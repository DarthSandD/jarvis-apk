package com.darrenai.jarvis

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.darrenai.jarvis.activity.SettingsActivity
import com.darrenai.jarvis.databinding.ActivityMainBinding
import com.darrenai.jarvis.model.ChatMessage
import com.darrenai.jarvis.services.VoiceListenerService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * MainActivity — JARVIS chat interface.
 *
 * Modern Android: edge-to-edge layout, Material 3, fluid animations,
 * dual-mode (online Hermes / offline local), voice I/O.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var tts: TextToSpeech
    private var voiceServiceBound = false
    private var voiceService: VoiceListenerService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val b = binder as VoiceListenerService.LocalBinder
            voiceService = b.service()
            voiceServiceBound = true
            updateVoiceButtonState()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            voiceService = null
            voiceServiceBound = false
            updateVoiceButtonState()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setupEdgeToEdge()
        setupChat()
        setupVoice()
        setupTTS()
        setupConnectivity()
        setupModeIndicator()
        setupSettingsButton()

        // Initialize greeting
        lifecycleScope.launch {
            chatAdapter.addMessage(
                ChatMessage(ChatMessage.Role.SYSTEM, "JARVIS initialized.")
            )
            delay(600)
            val mode = ConnectivityManager(this@MainActivity).getCurrentMode()
            when (mode) {
                ConnectivityManager.Mode.ONLINE -> {
                    chatAdapter.addMessage(
                        ChatMessage(ChatMessage.Role.ASSISTANT,
                            "✓ Connected to Hermes AI.\nI'm ready. Ask me anything."
                        )
                    )
                }
                ConnectivityManager.Mode.OFFLINE -> {
                    chatAdapter.addMessage(
                        ChatMessage(ChatMessage.Role.ASSISTANT,
                            "⚡ Running offline mode.\nI have local intelligence. Connect to the internet to unlock full Hermes AI."
                        )
                    )
                }
                else -> {
                    chatAdapter.addMessage(
                        ChatMessage(ChatMessage.Role.ASSISTANT,
                            "Connecting..."
                        )
                    )
                }
            }
        }
    }

    private fun setupEdgeToEdge() {
        // Enable edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbar.setPadding(0, systemBars.top, 0, 0)
            val bottomInset = systemBars.bottom
            binding.layoutInput.setPadding(
                binding.layoutInput.paddingLeft,
                binding.layoutInput.paddingTop,
                binding.layoutInput.paddingRight,
                bottomInset + 8
            )
            insets
        }

        // Hide system bars
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    private fun setupChat() {
        chatAdapter = ChatAdapter()
        binding.recyclerViewChat.apply {
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
            itemAnimator = null // Disable default animations for smoother scroll
        }

        chatAdapter.setOnNewMessageListener {
            binding.recyclerViewChat.post {
                binding.recyclerViewChat.scrollToPosition(chatAdapter.itemCount - 1)
            }
        }

        binding.btnSend.setOnClickListener {
            val text = binding.editTextMessage.text?.toString()?.trim()
            if (!text.isNullOrEmpty()) {
                sendMessage(text)
                binding.editTextMessage.text?.clear()
                updateEmptyState()
            }
        }

        binding.editTextMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val text = binding.editTextMessage.text?.toString()?.trim()
                if (!text.isNullOrEmpty()) {
                    sendMessage(text)
                    binding.editTextMessage.text?.clear()
                    updateEmptyState()
                }
                true
            } else {
                false
            }
        }
    }

    private fun setupVoice() {
        binding.btnVoice.setOnClickListener {
            if (voiceServiceBound && voiceService != null) {
                if (VoiceListenerService.isListening) {
                    voiceService?.stopListening()
                    hideVoiceWaves()
                } else {
                    voiceService?.startListening { transcript ->
                        runOnUiThread {
                            binding.editTextMessage.setText(transcript)
                            binding.editTextMessage.setSelection(transcript.length)
                        }
                    }
                    showVoiceWaves()
                }
            } else {
                val intent = Intent(this, VoiceListenerService::class.java)
                startService(intent)
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            }
        }
    }

    private fun showVoiceWaves() {
        binding.layoutVoiceWaves.visibility = View.VISIBLE
        // Simple animation - could be replaced with proper ValueAnimator
        val waves = listOf(
            binding.voiceWave1,
            binding.voiceWave2,
            binding.voiceWave3,
            binding.voiceWave4,
            binding.voiceWave5
        )
        waves.forEach { wave ->
            wave.layoutParams.height = resources.getDimensionPixelSize(R.dimen.voice_wave_min)
            wave.requestLayout()
        }
    }

    private fun hideVoiceWaves() {
        binding.layoutVoiceWaves.visibility = View.GONE
    }

    private fun updateVoiceButtonState() {
        if (VoiceListenerService.isListening) {
            binding.btnVoice.setImageResource(R.drawable.ic_mic_active)
            binding.btnVoice.setBackgroundResource(R.drawable.bg_voice_active)
        } else {
            binding.btnVoice.setImageResource(R.drawable.ic_mic)
            binding.btnVoice.setBackgroundResource(R.drawable.bg_voice_inactive)
        }
    }

    private fun setupTTS() {
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
                tts.setSpeechRate(0.85f)
                tts.setPitch(1.0f)
            }
        }
    }

    private fun speak(text: String) {
        if (tts.isSpeaking) tts.stop()
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "jarvis_tts")
    }

    private fun setupConnectivity() {
        val connectivityManager = ConnectivityManager(this)
        connectivityManager.startMonitoring()
        connectivityManager.listener = { mode ->
            runOnUiThread {
                updateModeIndicator(mode)
                val statusText = when (mode) {
                    ConnectivityManager.Mode.ONLINE -> "Online • Hermes AI"
                    ConnectivityManager.Mode.OFFLINE -> "Offline • Local AI"
                    ConnectivityManager.Mode.UNKNOWN -> "Connecting..."
                }
                binding.txtModeStatus.text = statusText
            }
        }
    }

    private fun setupModeIndicator() {
        binding.txtModeStatus.text = "Connecting..."
        binding.viewStatusDot.setBackgroundResource(R.drawable.bg_voice_active)
        binding.viewStatusDot.background?.mutate()?.setTint(getColor(R.color.jarvis_gray))
    }

    private fun updateModeIndicator(mode: ConnectivityManager.Mode) {
        val color = when (mode) {
            ConnectivityManager.Mode.ONLINE -> getColor(R.color.jarvis_green)
            ConnectivityManager.Mode.OFFLINE -> getColor(R.color.jarvis_orange)
            ConnectivityManager.Mode.UNKNOWN -> getColor(R.color.jarvis_gray)
        }
        binding.viewStatusDot.background?.setTint(color)
    }

    private fun setupSettingsButton() {
        binding.btnSettingsTop.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateEmptyState() {
        if (chatAdapter.itemCount == 0) {
            binding.emptyState.visibility = View.VISIBLE
            binding.recyclerViewChat.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.recyclerViewChat.visibility = View.VISIBLE
        }
    }

    private fun sendMessage(text: String) {
        vibrate()
        updateEmptyState()
        chatAdapter.addMessage(ChatMessage(ChatMessage.Role.USER, text))
        updateEmptyState()

        chatAdapter.addMessage(ChatMessage(ChatMessage.Role.ASSISTANT, "Thinking..."))
        val thinkingIndex = chatAdapter.itemCount - 1

        lifecycleScope.launch {
            val connectivityManager = ConnectivityManager(this@MainActivity)
            val mode = connectivityManager.getCurrentMode()

            try {
                val response = connectivityManager.sendMessage(
                    listOf(ChatMessage(ChatMessage.Role.USER, text)),
                    mode
                ) { progress ->
                    chatAdapter.updateMessage(thinkingIndex, "Thinking... $progress")
                }

                chatAdapter.removeMessageAt(thinkingIndex)
                chatAdapter.addMessage(ChatMessage(ChatMessage.Role.ASSISTANT, response))
                speak(response)
                updateEmptyState()
            } catch (e: Exception) {
                chatAdapter.removeMessageAt(thinkingIndex)
                val errorMsg = "I'm sorry, I encountered an error.\n${e.message}"
                chatAdapter.addMessage(ChatMessage(ChatMessage.Role.ASSISTANT, errorMsg))
                updateEmptyState()
            }
        }
    }

    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator.vibrate(
                VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(20)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!voiceServiceBound) {
            val intent = Intent(this, VoiceListenerService::class.java)
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (voiceServiceBound) {
            unbindService(serviceConnection)
            voiceServiceBound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (tts.isSpeaking) tts.stop()
        tts.shutdown()
        ConnectivityManager(this).stopMonitoring()
    }
}
