package com.darrenai.jarvis.services

import android.content.Context
import android.speech.tts.TextToSpeech
import android.os.Build
import android.os.Bundle
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
 * Jarvis speaks like a real person using paced speech synthesis.
 * Each character/word gets a micro-delay between utterances for natural flow.
 *
 * Personality traits:
 * - Tone: precise, confident, slightly dry wit
 * - Pace: deliberate but not slow
 * - Vocabulary: technical but accessible
 */
class JarvisVoiceService(private val context: Context) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var tts: TextToSpeech? = null
    private var isSpeaking = false
    private var currentJob: Job? = null

    companion object {
        private const val VOICE_SPEED_DEFAULT = 0.92f
        private const val VOICE_PITCH_DEFAULT = 1.0f
        private const val CHARACTER_DELAY_MS = 8L
        private const val WORD_DELAY_MS = 35L
        private const val PARAGRAPH_DELAY_MS = 300L

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

        @Volatile
        private var instance: JarvisVoiceService? = null

        fun getInstance(context: Context): JarvisVoiceService {
            return instance ?: synchronized(this) {
                instance ?: JarvisVoiceService(context.applicationContext).also { instance = it }
            }
        }
    }

    init {
        initTts()
    }

    private fun initTts() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setSpeechRate(VOICE_SPEED_DEFAULT)
                tts?.setPitch(VOICE_PITCH_DEFAULT)
            }
        }
    }

    /** Speak text with Jarvis pacing — segment-based natural flow. */
    fun speak(text: String, onDone: () -> Unit = {}) {
        if (text.isBlank()) { onDone(); return }
        if (isSpeaking) {
            stop()
        }

        isSpeaking = true
        currentJob = scope.launch {
            val segments = segmentText(text)
            for (i in segments.indices) {
                val segment = segments[i]
                if (!isSpeaking) break

                val utteranceId = UUID.randomUUID().toString()
                tts?.speak(segment, TextToSpeech.QUEUE_ADD, null, utteranceId)

                if (i < segments.size - 1) {
                    delay(calculateInterSegmentDelay(segment))
                }
            }
            isSpeaking = false
            onDone()
        }
    }

    /** Stop speaking immediately. */
    fun stop() {
        isSpeaking = false
        currentJob?.cancel()
        currentJob = null
        tts?.stop()
    }

    /** Check if currently speaking. */
    fun isCurrentlySpeaking(): Boolean = isSpeaking

    /** Configure voice parameters. */
    fun configure(speed: Float = VOICE_SPEED_DEFAULT, pitch: Float = VOICE_PITCH_DEFAULT) {
        tts?.setSpeechRate(speed.coerceIn(0.5f, 1.5f))
        tts?.setPitch(pitch.coerceIn(0.5f, 2.0f))
    }

    /** Destroy TTS engine. */
    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
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
}
