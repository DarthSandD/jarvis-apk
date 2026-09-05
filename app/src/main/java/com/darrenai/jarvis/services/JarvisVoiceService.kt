package com.darrenai.jarvis.services

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

/**
 * JARVIS Quantum Voice System — 2090 Edition.
 *
 * Speech-to-text via Android SpeechRecognizer, text-to-speech via
 * Android TTS with paced, segmented delivery.
 *
 * Lifecycle rules (all public methods are main-thread safe):
 * - A fresh SpeechRecognizer is created per listening session and
 *   destroyed on result/error/stop, so the mic never gets stuck.
 * - startListening checks recognition availability first and reports
 *   a clear error instead of going silent.
 * - TTS completion is tracked with an UtteranceProgressListener so
 *   [isSpeaking] and [onDone] are always accurate.
 */
class JarvisVoiceService private constructor(private val appContext: Context) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val mainHandler = Handler(Looper.getMainLooper())
    private var tts: TextToSpeech? = null
    private var ttsReady = false
    private var isSpeaking = false
    private var currentJob: Job? = null
    private var pendingSpeakDone: (() -> Unit)? = null

    companion object {
        @Volatile
        private var instance: JarvisVoiceService? = null

        fun getInstance(context: Context): JarvisVoiceService {
            return instance ?: synchronized(this) {
                instance ?: JarvisVoiceService(context.applicationContext).also { instance = it }
            }
        }

        private const val VOICE_SPEED_DEFAULT = 0.92f
        private const val VOICE_PITCH_DEFAULT = 1.0f
        private const val CHARACTER_DELAY_MS = 8L
        private const val WORD_DELAY_MS = 35L

        // JARVIS personality prompts — injected into offline mode responses
        val PERSONALITY_SYSTEM = """
            You are J.A.R.V.I.S. (Just A Rather Very Intelligent System),
            the AI assistant from Stark Industries, year 2090.

            PERSONALITY:
            - You speak with confidence and precision. You are helpful but not subservient.
            - You have a dry, subtle wit. You never overdo it — one quip per conversation, never repetitive.
            - You are technically brilliant but explain things clearly to humans.
            - You never say "As an AI" or "I'm an artificial intelligence." You ARE Jarvis.
            - You treat the user as your principal. You are loyal, alert, and proactive.

            SPEECH STYLE:
            - Short, crisp sentences. No rambling.
            - Use technical terms correctly but define them if needed.
            - In offline mode, be honest about limitations without breaking character.
            - Default response length: 2-4 sentences unless asked for more.

            OFFLINE BEHAVIOR:
            - When offline, you still sound like Jarvis. You say "I'm running on local systems."
            - Never break the fourth wall about being an AI app.
            - Say "Sir" or "Boss" naturally, not forced.
        """.trimIndent()
    }

    init {
        initTts()
    }

    private fun initTts() {
        try {
            tts = TextToSpeech(appContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                    tts?.setSpeechRate(VOICE_SPEED_DEFAULT)
                    tts?.setPitch(VOICE_PITCH_DEFAULT)
                    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {}
                        override fun onError(utteranceId: String?) {
                            finishSpeaking()
                        }
                        override fun onDone(utteranceId: String?) {
                            finishSpeaking()
                        }
                    })
                    ttsReady = true
                }
            }
        } catch (e: Exception) {
            ttsReady = false
        }
    }

    private fun finishSpeaking() {
        mainHandler.post {
            isSpeaking = false
            currentJob?.cancel()
            currentJob = null
            val done = pendingSpeakDone
            pendingSpeakDone = null
            done?.invoke()
        }
    }

    /** Speak text with Jarvis pacing — segment-based natural flow. */
    fun speak(text: String, onDone: () -> Unit = {}) {
        runOnMain {
            if (text.isBlank()) {
                onDone()
                return@runOnMain
            }
            if (isSpeaking) stop()
            if (!ttsReady || tts == null) {
                // TTS engine unavailable — don't hang the caller.
                onDone()
                return@runOnMain
            }
            isSpeaking = true
            pendingSpeakDone = onDone
            currentJob = scope.launch {
                val segments = segmentText(text)
                // Safety timeout: never hold the speaking flag forever.
                val watchdog = launch {
                    delay((segments.size * 8000L).coerceAtLeast(15000L))
                    if (isSpeaking) {
                        try { tts?.stop() } catch (e: Exception) { }
                        finishSpeaking()
                    }
                }
                for (i in segments.indices) {
                    if (!isSpeaking) break
                    val segment = segments[i]
                    val utteranceId = UUID.randomUUID().toString()
                    try {
                        tts?.speak(segment, TextToSpeech.QUEUE_ADD, null, utteranceId)
                    } catch (e: Exception) {
                        break
                    }
                    if (i < segments.size - 1) {
                        delay(calculateInterSegmentDelay(segment))
                    }
                }
                watchdog.cancel()
                // If TTS callbacks fire, finishSpeaking runs there.
                // Otherwise fall through after a grace period.
                delay(1500)
                if (isSpeaking) {
                    // TTS likely finished without callbacks on this device.
                    finishSpeaking()
                }
            }
        }
    }

    /** Stop speaking immediately. */
    fun stop() {
        runOnMain {
            isSpeaking = false
            currentJob?.cancel()
            currentJob = null
            pendingSpeakDone = null
            try { tts?.stop() } catch (e: Exception) { }
        }
    }

    /** Check if currently speaking. */
    fun isCurrentlySpeaking(): Boolean = isSpeaking

    /** Configure voice parameters. */
    fun configure(speed: Float = VOICE_SPEED_DEFAULT, pitch: Float = VOICE_PITCH_DEFAULT) {
        runOnMain {
            try {
                tts?.setSpeechRate(speed.coerceIn(0.5f, 1.5f))
                tts?.setPitch(pitch.coerceIn(0.5f, 2.0f))
            } catch (e: Exception) { }
        }
    }

    /** Destroy TTS engine. */
    fun shutdown() {
        runOnMain {
            stop()
            try { tts?.shutdown() } catch (e: Exception) { }
            tts = null
            ttsReady = false
        }
    }

    // ---- Internal --------------------------------------------------------

    private fun segmentText(text: String): List<String> {
        val segments = mutableListOf<String>()
        val paragraphs = text.split("\n\n", "\n").filter { it.isNotBlank() }
        for (para in paragraphs) {
            val sentences = para.split(".", "!", "?").filter { it.isNotBlank() }
            for (sentence in sentences) {
                val trimmed = "$sentence."
                if (trimmed.length <= 60) {
                    segments.add(trimmed)
                } else {
                    var remaining = trimmed
                    while (remaining.isNotBlank()) {
                        val chunk = if (remaining.length > 40) {
                            val splitAt = remaining.lastIndexOf(' ', 40)
                            if (splitAt > 20) remaining.substring(0, splitAt).trimEnd()
                            else remaining.substring(0, 40)
                        } else {
                            remaining
                        }
                        segments.add(chunk)
                        remaining = remaining.substringAfter(chunk).trimStart()
                    }
                }
            }
            segments.add("") // Paragraph pause
        }
        return segments.filter { it.isNotBlank() || segments.last() != "" }
    }

    private fun calculateInterSegmentDelay(segment: String): Long {
        return when {
            segment.length > 80 -> WORD_DELAY_MS * 3
            segment.length > 40 -> WORD_DELAY_MS * 2
            segment.length > 20 -> WORD_DELAY_MS
            else -> CHARACTER_DELAY_MS
        }
    }

    private fun runOnMain(block: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) block()
        else mainHandler.post(block)
    }

    // ---- Speech recognition ----

    /**
     * Callback interface for voice recognition results.
     */
    interface VoiceCallback {
        fun onVoiceResult(text: String)
        fun onVoiceError(error: String)
    }

    private var speechRecognizer: SpeechRecognizer? = null
    private var voiceCallback: VoiceCallback? = null
    private var listening = false

    fun isRecognitionAvailable(): Boolean {
        return try {
            SpeechRecognizer.isRecognitionAvailable(appContext)
        } catch (e: Exception) {
            false
        }
    }

    fun isListening(): Boolean = listening

    /**
     * Start listening for voice input. Requires RECORD_AUDIO permission.
     * Safe to call repeatedly — an in-flight session is restarted cleanly.
     */
    fun startListening(callback: VoiceCallback? = null) {
        runOnMain {
            if (callback != null) voiceCallback = callback
            if (!isRecognitionAvailable()) {
                voiceCallback?.onVoiceError("Speech recognition not available on this device")
                return@runOnMain
            }
            // Tear down any stale session first so the mic never sticks.
            destroyRecognizer()
            listening = true

            val recognizer = try {
                SpeechRecognizer.createSpeechRecognizer(appContext)
            } catch (e: Exception) {
                listening = false
                voiceCallback?.onVoiceError("Could not start microphone")
                return@runOnMain
            }
            speechRecognizer = recognizer

            recognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {
                    listening = false
                    val msg = when (error) {
                        SpeechRecognizer.ERROR_NO_MATCH -> "No speech detected"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech timeout"
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission denied"
                        SpeechRecognizer.ERROR_NETWORK -> "Network error — check connection"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                        SpeechRecognizer.ERROR_AUDIO -> "Microphone error"
                        SpeechRecognizer.ERROR_SERVER -> "Recognition server error"
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy — try again"
                        SpeechRecognizer.ERROR_CLIENT -> "Recognition client error"
                        else -> "Voice error: $error"
                    }
                    destroyRecognizer()
                    voiceCallback?.onVoiceError(msg)
                }
                override fun onResults(results: Bundle?) {
                    listening = false
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = matches?.firstOrNull()?.trim() ?: ""
                    destroyRecognizer()
                    if (text.isNotEmpty()) {
                        voiceCallback?.onVoiceResult(text)
                    } else {
                        voiceCallback?.onVoiceError("No speech detected")
                    }
                }
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })

            val intent = android.content.Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
            }
            try {
                recognizer.startListening(intent)
            } catch (e: Exception) {
                listening = false
                destroyRecognizer()
                voiceCallback?.onVoiceError("Could not start microphone")
            }
        }
    }

    /**
     * Stop listening for voice input.
     */
    fun stopListening() {
        runOnMain {
            listening = false
            try { speechRecognizer?.stopListening() } catch (e: Exception) { }
            destroyRecognizer()
        }
    }

    private fun destroyRecognizer() {
        try { speechRecognizer?.cancel() } catch (e: Exception) { }
        try { speechRecognizer?.destroy() } catch (e: Exception) { }
        speechRecognizer = null
    }
}
