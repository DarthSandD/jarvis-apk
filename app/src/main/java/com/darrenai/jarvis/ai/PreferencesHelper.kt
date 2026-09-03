package com.darrenai.jarvis.ai

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Encrypted preferences storage for JARVIS AI provider settings.
 *
 * Uses AndroidX Security Crypto for API keys, falls back to plain
 * SharedPreferences if the keystore is unavailable on this device.
 */
class PreferencesHelper(context: Context) {

    companion object {
        private const val ENCRYPTED_PREFS_NAME = "jarvis_secure_prefs"
        private const val PLAIN_PREFS_NAME = "jarvis_settings"

        // Plain (non-sensitive) keys
        const val KEY_SELECTED_PROVIDER = "selected_provider"
        const val KEY_OPENAI_MODEL = "openai_model"
        const val KEY_LOCAL_ENDPOINT = "local_endpoint"
        const val KEY_LOCAL_MODEL = "local_model"

        // Encrypted keys
        const val KEY_OPENAI_API_KEY = "openai_api_key"

        // Defaults
        const val DEFAULT_OPENAI_MODEL = "gpt-4o-mini"
        const val DEFAULT_LOCAL_ENDPOINT = "http://localhost:8081/v1"
        const val DEFAULT_LOCAL_MODEL = "llama-3.1-8b"
        const val DEFAULT_PROVIDER = "hermes"
    }

    private val encryptedPrefs: SharedPreferences? = try {
        val masterKey = MasterKey.Builder(context.applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context.applicationContext,
            ENCRYPTED_PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        // Keystore failure — return null and use plain prefs fallback
        null
    }

    private val plainPrefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PLAIN_PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * The currently selected AI provider. Defaults to Hermes if nothing is stored.
     */
    var selectedProvider: AiProvider
        get() = AiProvider.fromId(plainPrefs.getString(KEY_SELECTED_PROVIDER, DEFAULT_PROVIDER))
        set(value) = plainPrefs.edit().putString(KEY_SELECTED_PROVIDER, value.id).apply()

    /**
     * The OpenAI model string, e.g. "gpt-4o-mini".
     */
    var openAiModel: String
        get() = plainPrefs.getString(KEY_OPENAI_MODEL, DEFAULT_OPENAI_MODEL) ?: DEFAULT_OPENAI_MODEL
        set(value) = plainPrefs.edit().putString(KEY_OPENAI_MODEL, value).apply()

    /**
     * The OpenAI API key. Stored in EncryptedSharedPreferences when available.
     */
    var openAiApiKey: String
        get() = encryptedPrefs?.getString(KEY_OPENAI_API_KEY, "")
            ?: plainPrefs.getString(KEY_OPENAI_API_KEY, "")
            ?: ""
        set(value) {
            if (encryptedPrefs != null) {
                encryptedPrefs.edit().putString(KEY_OPENAI_API_KEY, value).apply()
            } else {
                plainPrefs.edit().putString(KEY_OPENAI_API_KEY, value).apply()
            }
        }

    /**
     * Local llama.cpp server endpoint, e.g. "http://localhost:8081/v1".
     */
    var localEndpoint: String
        get() = plainPrefs.getString(KEY_LOCAL_ENDPOINT, DEFAULT_LOCAL_ENDPOINT) ?: DEFAULT_LOCAL_ENDPOINT
        set(value) = plainPrefs.edit().putString(KEY_LOCAL_ENDPOINT, value).apply()

    /**
     * Local model name to request from the llama.cpp server.
     */
    var localModel: String
        get() = plainPrefs.getString(KEY_LOCAL_MODEL, DEFAULT_LOCAL_MODEL) ?: DEFAULT_LOCAL_MODEL
        set(value) = plainPrefs.edit().putString(KEY_LOCAL_MODEL, value).apply()

    /**
     * Migration helper: move an API key stored in plain prefs to encrypted.
     */
    fun migrateToEncrypted() {
        val plainKey = plainPrefs.getString(KEY_OPENAI_API_KEY, null)
        if (!plainKey.isNullOrEmpty() && encryptedPrefs != null) {
            encryptedPrefs.edit().putString(KEY_OPENAI_API_KEY, plainKey).apply()
            plainPrefs.edit().remove(KEY_OPENAI_API_KEY).apply()
        }
    }
}