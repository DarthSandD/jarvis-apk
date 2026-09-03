package com.darrenai.jarvis.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.darrenai.jarvis.R
import com.darrenai.jarvis.ai.AiProvider
import com.darrenai.jarvis.ai.AiService
import com.darrenai.jarvis.ai.PreferencesHelper
import com.darrenai.jarvis.databinding.ActivitySettingsBinding
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: PreferencesHelper
    private lateinit var aiService: AiService

    companion object {
        private val OPENAI_MODELS = listOf(
            "gpt-4o",
            "gpt-4o-mini",
            "gpt-4-turbo",
            "gpt-3.5-turbo"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prefs = PreferencesHelper(this)
        aiService = AiService.getInstance(this)

        setupToolbar()
        setupAiProviderSection()
        loadSettings()
        bindListeners()
        runHealthChecks()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupAiProviderSection() {
        // Set up OpenAI model spinner
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            OPENAI_MODELS
        )
        binding.spinnerOpenaiModel.adapter = spinnerAdapter

        // Pre-select saved model
        val savedModel = prefs.openAiModel
        val modelIndex = OPENAI_MODELS.indexOf(savedModel)
        if (modelIndex >= 0) {
            binding.spinnerOpenaiModel.setSelection(modelIndex)
        }

        // Update radio buttons based on saved provider
        when (prefs.selectedProvider) {
            AiProvider.OpenAI -> binding.radioProviderOpenai.isChecked = true
            AiProvider.Local -> binding.radioProviderLocal.isChecked = true
            AiProvider.Hermes -> binding.radioProviderHermes.isChecked = true
        }

        // Hermes endpoint display
        val hermesIp = getSharedPreferences("jarvis_settings", MODE_PRIVATE)
            .getString("server_ip", "10.212.104.140") ?: "10.212.104.140"
        val hermesPort = getSharedPreferences("jarvis_settings", MODE_PRIVATE)
            .getInt("server_port", 20128)
        binding.txtHermesEndpoint.text = "$hermesIp:$hermesPort"
    }

    private fun loadSettings() {
        binding.settingServerIp.setText(
            getSharedPreferences("jarvis_settings", MODE_PRIVATE)
                .getString("server_ip", "10.212.104.140")
        )
        binding.settingServerPort.setText(
            getSharedPreferences("jarvis_settings", MODE_PRIVATE)
                .getInt("server_port", 20128).toString()
        )
        binding.settingVoiceMode.setText(
            getSharedPreferences("jarvis_settings", MODE_PRIVATE)
                .getString("voice_mode", "continuous")
        )
        binding.settingVoiceLanguage.setText(
            getSharedPreferences("jarvis_settings", MODE_PRIVATE)
                .getString("voice_language", "en-US")
        )
        binding.settingVoiceTimeout.setText(
            getSharedPreferences("jarvis_settings", MODE_PRIVATE)
                .getInt("voice_timeout_ms", 5000).toString()
        )
        binding.settingSampleRate.setText(
            getSharedPreferences("jarvis_settings", MODE_PRIVATE)
                .getInt("sample_rate", 16000).toString()
        )
        binding.settingAutoConnect.isChecked =
            getSharedPreferences("jarvis_settings", MODE_PRIVATE)
                .getBoolean("auto_connect", true)
        binding.settingNotificationEnabled.isChecked =
            getSharedPreferences("jarvis_settings", MODE_PRIVATE)
                .getBoolean("notification_enabled", true)

        // Load AI provider settings
        binding.settingOpenaiApiKey.setText(prefs.openAiApiKey)
        binding.settingLocalEndpoint.setText(prefs.localEndpoint)
        binding.settingLocalModel.setText(prefs.localModel)
    }

    private fun bindListeners() {
        binding.btnSave.setOnClickListener {
            saveSettings()
            Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.btnReset.setOnClickListener {
            resetSettings()
        }

        // Test connection buttons
        binding.btnTestOpenai.setOnClickListener {
            testOpenAiConnection()
        }
        binding.btnTestLocal.setOnClickListener {
            testLocalConnection()
        }
        binding.btnTestHermes.setOnClickListener {
            testHermesConnection()
        }
        binding.btnDetectLocal.setOnClickListener {
            autoDetectLocal()
        }
    }

    private fun saveSettings() {
        // Save legacy settings
        getSharedPreferences("jarvis_settings", MODE_PRIVATE).edit().apply {
            putString("server_ip", binding.settingServerIp.text.toString())
            putInt("server_port", binding.settingServerPort.text.toString().toIntOrNull() ?: 20128)
            putString("voice_mode", binding.settingVoiceMode.text.toString())
            putString("voice_language", binding.settingVoiceLanguage.text.toString())
            putInt("voice_timeout_ms", binding.settingVoiceTimeout.text.toString().toIntOrNull() ?: 5000)
            putInt("sample_rate", binding.settingSampleRate.text.toString().toIntOrNull() ?: 16000)
            putBoolean("auto_connect", binding.settingAutoConnect.isChecked)
            putBoolean("notification_enabled", binding.settingNotificationEnabled.isChecked)
            apply()
        }

        // Save AI provider settings
        prefs.selectedProvider = when {
            binding.radioProviderOpenai.isChecked -> AiProvider.OpenAI
            binding.radioProviderLocal.isChecked -> AiProvider.Local
            else -> AiProvider.Hermes
        }
        prefs.openAiApiKey = binding.settingOpenaiApiKey.text.toString()
        prefs.openAiModel = binding.spinnerOpenaiModel.selectedItem.toString()
        prefs.localEndpoint = binding.settingLocalEndpoint.text.toString()
        prefs.localModel = binding.settingLocalModel.text.toString()

        // Migrate any plain-text keys to encrypted
        prefs.migrateToEncrypted()
    }

    private fun resetSettings() {
        // Reset legacy settings
        getSharedPreferences("jarvis_settings", MODE_PRIVATE).edit().apply {
            putString("server_ip", "10.212.104.140")
            putInt("server_port", 20128)
            putString("voice_mode", "continuous")
            putString("voice_language", "en-US")
            putInt("voice_timeout_ms", 5000)
            putInt("sample_rate", 16000)
            putBoolean("auto_connect", true)
            putBoolean("notification_enabled", true)
            apply()
        }

        // Reset AI settings to defaults
        binding.settingOpenaiApiKey.setText("")
        binding.settingLocalEndpoint.setText(PreferencesHelper.DEFAULT_LOCAL_ENDPOINT)
        binding.settingLocalModel.setText(PreferencesHelper.DEFAULT_LOCAL_MODEL)
        binding.radioProviderHermes.isChecked = true

        Toast.makeText(this, R.string.settings_reset, Toast.LENGTH_SHORT).show()
    }

    private fun runHealthChecks() {
        updateStatusDot(binding.statusDotOpenai, binding.txtStatusOpenai, "Checking...")
        updateStatusDot(binding.statusDotLocal, binding.txtStatusLocal, "Checking...")
        updateStatusDot(binding.statusDotHermes, binding.txtStatusHermes, "Checking...")

        lifecycleScope.launch {
            val openaiError = aiService.healthCheck(AiProvider.OpenAI)
            runOnUiThread {
                if (openaiError == null) {
                    updateStatusDot(binding.statusDotOpenai, binding.txtStatusOpenai, "Connected", R.color.jarvis_green)
                } else {
                    val text = when (openaiError) {
                        is com.darrenai.jarvis.ai.AiError.NoApiKey -> "Not configured"
                        else -> "Unreachable"
                    }
                    val color = if (openaiError is com.darrenai.jarvis.ai.AiError.NoApiKey) {
                        R.color.jarvis_gray
                    } else {
                        R.color.jarvis_orange
                    }
                    updateStatusDot(binding.statusDotOpenai, binding.txtStatusOpenai, text, color)
                }
            }
        }

        lifecycleScope.launch {
            val localError = aiService.healthCheck(AiProvider.Local)
            runOnUiThread {
                if (localError == null) {
                    updateStatusDot(binding.statusDotLocal, binding.txtStatusLocal, "Connected", R.color.jarvis_green)
                } else {
                    updateStatusDot(binding.statusDotLocal, binding.txtStatusLocal, "Unreachable", R.color.jarvis_orange)
                }
            }
        }

        lifecycleScope.launch {
            val hermesError = aiService.healthCheck(AiProvider.Hermes)
            runOnUiThread {
                if (hermesError == null) {
                    updateStatusDot(binding.statusDotHermes, binding.txtStatusHermes, "Connected", R.color.jarvis_green)
                } else {
                    updateStatusDot(binding.statusDotHermes, binding.txtStatusHermes, "Unreachable", R.color.jarvis_orange)
                }
            }
        }
    }

    private fun testOpenAiConnection() {
        val key = binding.settingOpenaiApiKey.text.toString()
        if (key.isBlank()) {
            Toast.makeText(this, "Please enter an API key first", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "Testing OpenAI...", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            val error = aiService.healthCheck(AiProvider.OpenAI)
            runOnUiThread {
                if (error == null) {
                    Toast.makeText(this@SettingsActivity, "✓ OpenAI connected!", Toast.LENGTH_SHORT).show()
                    updateStatusDot(binding.statusDotOpenai, binding.txtStatusOpenai, "Connected", R.color.jarvis_green)
                } else {
                    Toast.makeText(this@SettingsActivity, "✗ ${error.message}", Toast.LENGTH_LONG).show()
                    updateStatusDot(binding.statusDotOpenai, binding.txtStatusOpenai, "Failed", R.color.jarvis_orange)
                }
            }
        }
    }

    private fun testLocalConnection() {
        val endpoint = binding.settingLocalEndpoint.text.toString()
        Toast.makeText(this, "Testing local server...", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            val error = aiService.healthCheck(AiProvider.Local)
            runOnUiThread {
                if (error == null) {
                    Toast.makeText(this@SettingsActivity, "✓ Local AI connected!", Toast.LENGTH_SHORT).show()
                    updateStatusDot(binding.statusDotLocal, binding.txtStatusLocal, "Connected", R.color.jarvis_green)
                } else {
                    Toast.makeText(this@SettingsActivity, "✗ ${error.message}", Toast.LENGTH_LONG).show()
                    updateStatusDot(binding.statusDotLocal, binding.txtStatusLocal, "Failed", R.color.jarvis_orange)
                }
            }
        }
    }

    private fun testHermesConnection() {
        Toast.makeText(this, "Testing Hermes...", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            val error = aiService.healthCheck(AiProvider.Hermes)
            runOnUiThread {
                if (error == null) {
                    Toast.makeText(this@SettingsActivity, "✓ Hermes connected!", Toast.LENGTH_SHORT).show()
                    updateStatusDot(binding.statusDotHermes, binding.txtStatusHermes, "Connected", R.color.jarvis_green)
                } else {
                    Toast.makeText(this@SettingsActivity, "✗ ${error.message}", Toast.LENGTH_LONG).show()
                    updateStatusDot(binding.statusDotHermes, binding.txtStatusHermes, "Failed", R.color.jarvis_orange)
                }
            }
        }
    }

    private fun autoDetectLocal() {
        Toast.makeText(this, "Detecting models...", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            val models = aiService.detectLocalModels(binding.settingLocalEndpoint.text.toString())
            runOnUiThread {
                if (models.isNotEmpty()) {
                    Toast.makeText(this@SettingsActivity, "Found: ${models.joinToString()}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@SettingsActivity, "No models found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateStatusDot(dot: View, label: android.widget.TextView, text: String, colorRes: Int? = null) {
        label.text = text
        val color = if (colorRes != null) getColor(colorRes) else getColor(R.color.jarvis_gray)
        dot.background?.mutate()?.setTint(color)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}