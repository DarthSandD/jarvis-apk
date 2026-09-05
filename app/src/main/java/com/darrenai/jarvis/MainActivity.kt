package com.darrenai.jarvis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.darrenai.jarvis.ai.AiService
import com.darrenai.jarvis.ai.AiProvider
import com.darrenai.jarvis.ai.StreamEvent
import com.darrenai.jarvis.model.ChatMessage
import com.darrenai.jarvis.services.JarvisVoiceService
import com.darrenai.jarvis.ui.face.FaceActivity

class MainActivity : AppCompatActivity(), JarvisVoiceService.VoiceCallback {

    private var voiceService: JarvisVoiceService? = null
    private var isListening = false
    private var isSpeaking = false
    private var currentResponse = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        voiceService = JarvisVoiceService.getInstance(this)

        // FAB: toggle listening
        findViewById<FloatingActionButton>(R.id.fab_voice_main)
            .setOnClickListener {
                if (isListening) stopListening()
                else startListening()
            }

        // FAB: toggle face visualizer
        findViewById<FloatingActionButton>(R.id.fab_face)
            .setOnClickListener {
                FaceActivity.show(this)
            }

        // Face toggle in action mode (top-right)
        findViewById<android.widget.ImageButton>(R.id.btn_face_toggle)
            .setOnClickListener {
                FaceActivity.show(this)
            }
    }

    private fun startListening() {
        isListening = true
        updateUI()
        FaceActivity.setState("listening", 0.3f, 0.2f)
        voiceService?.startListening(this@MainActivity)
    }

    private fun stopListening() {
        isListening = false
        voiceService?.stopListening()
        updateUI()
    }

    private fun updateUI() {
        val fab = findViewById<FloatingActionButton>(R.id.fab_voice_main)
        val status = findViewById<android.widget.TextView>(R.id.txt_main_status)
        val ring = findViewById<android.view.View>(R.id.voice_ring_outer)
        val waveform = findViewById<android.widget.LinearLayout>(R.id.layout_voice_waveform)
        val transcriptScroll = findViewById<android.widget.ScrollView>(R.id.scroll_transcript)

        if (isListening) {
            fab.setImageResource(R.drawable.ic_mic_active)
            status.text = getString(R.string.voice_listening)
            ring.alpha = 0.6f
            waveform.visibility = android.view.View.VISIBLE
            transcriptScroll.visibility = android.view.View.VISIBLE
        } else {
            fab.setImageResource(R.drawable.ic_mic)
            status.text = getString(R.string.voice_tap_to_speak)
            ring.alpha = 0.3f
            waveform.visibility = android.view.View.GONE
        }
    }

    // ---- VoiceCallback ----
    override fun onVoiceResult(text: String) {
        findViewById<android.widget.TextView>(R.id.txt_user_transcript).apply {
            this.text = text
            visibility = android.view.View.VISIBLE
        }
        findViewById<android.widget.TextView>(R.id.txt_main_status).text = getString(R.string.voice_processing)
        FaceActivity.setState("thinking", 0.6f, 0.4f)

        currentResponse = StringBuilder()
        val history = listOf(
            ChatMessage(ChatMessage.Role.USER, text)
        )
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            AiService.getInstance(this@MainActivity)
                .chat(history, AiProvider.Hermes) { event ->
                    when (event) {
                        is StreamEvent.Delta -> {
                            currentResponse.append(event.text)
                        }
                        is StreamEvent.Done -> {
                            isSpeaking = true
                            FaceActivity.setState("speaking", 0.8f, 0.6f)
                            voiceService?.speak(currentResponse.toString()) {
                                isSpeaking = false
                                FaceActivity.setState("idle", 0f, 0f)
                                findViewById<android.widget.TextView>(R.id.txt_main_status)
                                    .text = getString(R.string.voice_tap_to_speak)
                            }
                            findViewById<android.widget.TextView>(R.id.txt_jarvis_response).apply {
                                this.text = currentResponse.toString()
                                visibility = android.view.View.VISIBLE
                            }
                        }
                        is StreamEvent.Error -> {
                            FaceActivity.setState("idle", 0f, 0f)
                            findViewById<android.widget.TextView>(R.id.txt_jarvis_response).apply {
                                this.text = "⚠️ ${event.error.message}"
                                visibility = android.view.View.VISIBLE
                            }
                            findViewById<android.widget.TextView>(R.id.txt_main_status)
                                .text = getString(R.string.voice_tap_to_speak)
                        }
                        else -> {}
                    }
                }
        }
    }

    override fun onVoiceError(error: String) {
        FaceActivity.setState("idle", 0f, 0f)
        findViewById<android.widget.TextView>(R.id.txt_main_status).text = error
        isListening = false
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceService?.stopListening()
        voiceService?.shutdown()
        FaceActivity.setState("idle", 0f, 0f)
    }
}
