package com.darrenai.jarvis.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.darrenai.jarvis.R
import com.darrenai.jarvis.databinding.ActivitySettingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: SharedPreferences

    companion object {
        private const val PREFS_NAME = "jarvis_settings"
        private const val KEY_SERVER_IP = "server_ip"
        private const val KEY_SERVER_PORT = "server_port"
        private const val KEY_VOICE_MODE = "voice_mode"
        private const val KEY_VOICE_LANGUAGE = "voice_language"
        private const val KEY_VOICE_TIMEOUT = "voice_timeout_ms"
        private const val KEY_SAMPLE_RATE = "sample_rate"
        private const val KEY_AUTO_CONNECT = "auto_connect"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_NOTIFICATION_ENABLED = "notification_enabled"
        private const val DEFAULT_SERVER_IP = "10.212.104.140"
        private const val DEFAULT_SERVER_PORT = 20128
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        setupToolbar()
        loadSettings()
        bindListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun loadSettings() {
        binding.settingServerIp.setText(prefs.getString(KEY_SERVER_IP, DEFAULT_SERVER_IP))
        binding.settingServerPort.setText(prefs.getInt(KEY_SERVER_PORT, DEFAULT_SERVER_PORT).toString())
        binding.settingVoiceMode.setText(prefs.getString(KEY_VOICE_MODE, "continuous"))
        binding.settingVoiceLanguage.setText(prefs.getString(KEY_VOICE_LANGUAGE, "en-US"))
        binding.settingVoiceTimeout.setText(prefs.getInt(KEY_VOICE_TIMEOUT, 5000).toString())
        binding.settingSampleRate.setText(prefs.getInt(KEY_SAMPLE_RATE, 16000).toString())
        binding.settingAutoConnect.isChecked = prefs.getBoolean(KEY_AUTO_CONNECT, true)
        binding.settingNotificationEnabled.isChecked = prefs.getBoolean(KEY_NOTIFICATION_ENABLED, true)
        updateTheme()
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
    }

    private fun saveSettings() {
        prefs.edit().apply {
            putString(KEY_SERVER_IP, binding.settingServerIp.text.toString())
            putInt(KEY_SERVER_PORT, binding.settingServerPort.text.toString().toIntOrNull() ?: DEFAULT_SERVER_PORT)
            putString(KEY_VOICE_MODE, binding.settingVoiceMode.text.toString())
            putString(KEY_VOICE_LANGUAGE, binding.settingVoiceLanguage.text.toString())
            putInt(KEY_VOICE_TIMEOUT, binding.settingVoiceTimeout.text.toString().toIntOrNull() ?: 5000)
            putInt(KEY_SAMPLE_RATE, binding.settingSampleRate.text.toString().toIntOrNull() ?: 16000)
            putBoolean(KEY_AUTO_CONNECT, binding.settingAutoConnect.isChecked)
            putBoolean(KEY_NOTIFICATION_ENABLED, binding.settingNotificationEnabled.isChecked)
            apply()
        }
    }

    private fun resetSettings() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                binding.settingServerIp.setText(DEFAULT_SERVER_IP)
                binding.settingServerPort.setText(DEFAULT_SERVER_PORT.toString())
                binding.settingVoiceMode.setText("continuous")
                binding.settingVoiceLanguage.setText("en-US")
                binding.settingVoiceTimeout.setText("5000")
                binding.settingSampleRate.setText("16000")
                binding.settingAutoConnect.isChecked = true
                binding.settingNotificationEnabled.isChecked = true
                Toast.makeText(this@SettingsActivity, R.string.settings_reset, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateTheme() {
        // Theme switching placeholder — plug in Material You dynamic color when ready
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
