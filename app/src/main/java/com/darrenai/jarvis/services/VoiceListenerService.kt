package com.darrenai.jarvis.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.core.app.NotificationCompat
import com.darrenai.jarvis.R
import com.darrenai.jarvis.MainActivity

class VoiceListenerService : Service() {

    private val binder = LocalBinder()
    private var listening = false
    private var speechRecognizer: SpeechRecognizer? = null
    private var onTranscript: ((String) -> Unit)? = null

    companion object {
        const val CHANNEL_ID = "jarvis_voice"
        const val NOTIFICATION_ID = 1
        const val REQUEST_VOICE = 1001
        @Volatile
        var isListening: Boolean = false
        private var instance: VoiceListenerService? = null

        fun getInstance(): VoiceListenerService {
            return instance ?: throw IllegalStateException("VoiceListenerService not bound")
        }
    }

    inner class LocalBinder : Binder() {
        fun service(): VoiceListenerService = this@VoiceListenerService
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onUnbind(intent: Intent?): Boolean {
        instance = null
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        stopListening()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Voice Listening",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Keeps JARVIS listening for voice commands"
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    fun startListening(callback: (String) -> Unit) {
        if (listening || isListening) return
        listening = true
        isListening = true
        onTranscript = callback
        startForeground(NOTIFICATION_ID, buildNotification())

        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            stopListening()
            return
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onResults(results: android.os.Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val transcript = matches?.firstOrNull() ?: ""
                    onTranscript?.invoke(transcript)
                    listening = false
                    isListening = false
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }

                override fun onError(error: Int) {
                    listening = false
                    isListening = false
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }

                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onReadyForSpeech(params: android.os.Bundle?) {}
                override fun onPartialResults(partialResults: android.os.Bundle?) {}
                override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
            })
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault())
        }
        speechRecognizer?.startListening(intent)
    }

    fun stopListening() {
        listening = false
        isListening = false
        onTranscript = null
        speechRecognizer?.stopListening()
        speechRecognizer?.destroy()
        speechRecognizer = null
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun buildNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("JARVIS listening")
            .setContentText("Tap to stop")
            .setSmallIcon(R.drawable.ic_mic_active)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setProgress(0, 0, true)
            .build()
    }
}