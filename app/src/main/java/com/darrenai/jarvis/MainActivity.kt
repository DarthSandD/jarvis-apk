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
import com.darrenai.jarvis.ai.AiProvider
import com.darrenai.jarvis.ai.AiService
import com.darrenai.jarvis.ai.StreamEvent
import com.darrenai.jarvis.databinding.ActivityMainBinding
import com.darrenai.jarvis.model.ChatMessage
import com.darrenai.jarvis.services.JarvisVoiceService
import com.darrenai.jarvis.services.VoiceListenerService
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.Locale

/**
 * MainActivity — JARVIS 2090 Edition.
 *
 * Voice-first AI assistant with Hermes OmniRoute integration.
 * Speaks like a real person using paced TTS (JarvisVoiceService).
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var chatAdapter: ChatAdapter
    private var voiceServiceBound = false
    private var voiceService: VoiceListenerService? = null

    private lateinit var aiService: AiService
    private var currentProvider: AiProvider = AiProvider.Hermes
    private var isAiResponding = false

    // 2090 JARVIS voice system
    private lateinit var jarvisVoice: JarvisVoiceService

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

        aiService = AiService.getInstance(this)
        jarvisVoice = JarvisVoiceService.getInstance(this)

        setupEdgeToEdge()
        setupChat()
        setupVoice()
        setupModeIndicator()
        setupSettingsButton()
        setupProviderChip()

        // Initialize greeting — Jarvis speaks when ready
        lifecycleScope.launch {
            chatAdapter.addMessage(
                ChatMessage(ChatMessage.Role.SYSTEM, "JARVIS initialized.")
            )
            delay(600)
            val provider = com.darrenai.jarvis.ai.PreferencesHelper(this@MainActivity).selectedProvider
            currentProvider = provider
            updateProviderChip()
            chatAdapter.addMessage(
                ChatMessage(ChatMessage.Role.ASSISTANT,
                    "Systems online. Hermes OmniRoute at 10.212.104.140:20128.\n" +
                    "I'm listening, boss. What do you need?"
                )
            )
            // Speak the greeting with paced TTS
            jarvisVoice.speak("Systems online. Hermes connected. I'm listening, boss.")
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh provider from saved prefs
        val saved = com.darrenai.jarvis.ai.PreferencesHelper(this).selectedProvider
        if (saved != currentProvider) {
            currentProvider = saved
            updateProviderChip()
        }
    }

    private fun setupEdgeToEdge() {
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
            itemAnimator = null
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
                            // Show what Jarvis heard
                            chatAdapter.addMessage(
                                ChatMessage(ChatMessage.Role.SYSTEM,
                                    "Heard: \"$transcript\""
                                )
                            )
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

    private fun setupModeIndicator() {
        binding.txtModeStatus.text = "Connecting..."
        binding.viewStatusDot.setBackgroundResource(R.drawable.bg_voice_active)
        binding.viewStatusDot.background?.mutate()?.setTint(getColor(R.color.jarvis_gray))
    }

    private fun setupProviderChip() {
        updateProviderChip()
        binding.chipProvider.setOnClickListener {
            showProviderSelector()
        }
    }

    private fun updateProviderChip() {
        binding.chipProvider.text = currentProvider.displayName
        val iconRes = when (currentProvider) {
            AiProvider.OpenAI -> R.drawable.ic_cloud
            AiProvider.Local -> R.drawable.ic_phone
            AiProvider.Hermes -> R.drawable.ic_server
        }
        binding.chipProvider.setChipIconResource(iconRes)
    }

    private fun showProviderSelector() {
        val bottomSheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.sheet_provider_selector, null)

        val openAiChip = view.findViewById<Chip>(R.id.chip_select_openai)
        val localChip = view.findViewById<Chip>(R.id.chip_select_local)
        val hermesChip = view.findViewById<Chip>(R.id.chip_select_hermes)

        openAiChip.setOnClickListener {
            switchProvider(AiProvider.OpenAI)
            bottomSheet.dismiss()
        }
        localChip.setOnClickListener {
            switchProvider(AiProvider.Local)
            bottomSheet.dismiss()
        }
        hermesChip.setOnClickListener {
            switchProvider(AiProvider.Hermes)
            bottomSheet.dismiss()
        }

        when (currentProvider) {
            AiProvider.OpenAI -> openAiChip.isChecked = true
            AiProvider.Local -> localChip.isChecked = true
            AiProvider.Hermes -> hermesChip.isChecked = true
        }

        val prefs = com.darrenai.jarvis.ai.PreferencesHelper(this)
        if (prefs.openAiApiKey.isBlank()) {
            openAiChip.isEnabled = false
            openAiChip.text = "OpenAI (needs API key)"
        }

        bottomSheet.setContentView(view)
        bottomSheet.show()
    }

    private fun switchProvider(provider: AiProvider) {
        currentProvider = provider
        com.darrenai.jarvis.ai.PreferencesHelper(this).selectedProvider = provider
        updateProviderChip()

        chatAdapter.addMessage(
            ChatMessage(ChatMessage.Role.SYSTEM,
                "Switched to ${provider.displayName}"
            )
        )
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

    private var serverIp: String = "10.212.104.140"

    private fun sendMessage(text: String) {
        if (isAiResponding) return

        vibrate()

        chatAdapter.addMessage(ChatMessage(ChatMessage.Role.USER, text))
        updateEmptyState()

        isAiResponding = true
        chatAdapter.addMessage(ChatMessage(ChatMessage.Role.ASSISTANT, "Thinking..."))
        val thinkingIndex = chatAdapter.itemCount - 1

        lifecycleScope.launch {
            try {
                val conversationHistory = chatAdapter.getMessagesForAi()

                // Add JARVIS personality prompt for offline mode
                val enhancedHistory = if (currentProvider == AiProvider.Local) {
                    val systemMsg = ChatMessage(
                        ChatMessage.Role.SYSTEM,
                        JarvisVoiceService.PERSONALITY_SYSTEM
                    )
                    listOf(systemMsg) + conversationHistory
                } else {
                    conversationHistory
                }

                aiService.chat(
                    messages = enhancedHistory,
                    preferredProvider = currentProvider
                ) { event ->
                    runOnUiThread {
                        when (event) {
                            is StreamEvent.Delta -> {
                                chatAdapter.updateMessage(thinkingIndex, event.text)
                            }
                            is StreamEvent.Status -> {
                                chatAdapter.updateMessage(thinkingIndex, event.message)
                            }
                            is StreamEvent.Done -> {
                                val finalText = event.fullText.ifEmpty {
                                    chatAdapter.getMessage(thinkingIndex)?.content ?: ""
                                }
                                chatAdapter.removeMessageAt(thinkingIndex)
                                chatAdapter.addMessage(
                                    ChatMessage(ChatMessage.Role.ASSISTANT, finalText)
                                )
                                // Speak with Jarvis paced voice
                                jarvisVoice.speak(finalText)
                                isAiResponding = false
                                updateEmptyState()
                            }
                            is StreamEvent.Error -> {
                                chatAdapter.removeMessageAt(thinkingIndex)
                                val errorMsg = "⚠ ${event.error.message}"
                                chatAdapter.addMessage(
                                    ChatMessage(ChatMessage.Role.ASSISTANT, errorMsg)
                                )
                                isAiResponding = false
                                updateEmptyState()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                chatAdapter.removeMessageAt(thinkingIndex)
                val errorMsg = "I'm sorry, I encountered an error.\n${e.message}"
                chatAdapter.addMessage(
                    ChatMessage(ChatMessage.Role.ASSISTANT, errorMsg)
                )
                isAiResponding = false
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
        if (jarvisVoice.isCurrentlySpeaking()) {
            jarvisVoice.stop()
        }
        jarvisVoice.shutdown()
        aiService.destroy()
    }
}
