package com.darrenai.jarvis.ai

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {

    companion object {
        private const val PREFS_NAME = "jarvis_settings"

        const val KEY_SELECTED_PROVIDER = "selected_provider"
        const val KEY_OPENAI_MODEL = "openai_model"
        const val KEY_LOCAL_ENDPOINT = "local_endpoint"
        const val KEY_LOCAL_MODEL = "local_model"
        const val KEY_OPENAI_API_KEY = "openai_api_key"

        const val DEFAULT_OPENAI_MODEL = "gpt-4o-mini"
        const val DEFAULT_LOCAL_ENDPOINT = "http://localhost:8081/v1"
        const val DEFAULT_LOCAL_MODEL = "llama-3.1-8b"
        const val DEFAULT_PROVIDER = "hermes"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getProvider(): String = prefs.getString(KEY_SELECTED_PROVIDER, DEFAULT_PROVIDER) ?: DEFAULT_PROVIDER
    fun setProvider(provider: String) = prefs.edit().putString(KEY_SELECTED_PROVIDER, provider).apply()

    fun getOpenaiModel(): String = prefs.getString(KEY_OPENAI_MODEL, DEFAULT_OPENAI_MODEL) ?: DEFAULT_OPENAI_MODEL
    fun setOpenaiModel(model: String) = prefs.edit().putString(KEY_OPENAI_MODEL, model).apply()

    fun getLocalEndpoint(): String = prefs.getString(KEY_LOCAL_ENDPOINT, DEFAULT_LOCAL_ENDPOINT) ?: DEFAULT_LOCAL_ENDPOINT
    fun setLocalEndpoint(url: String) = prefs.edit().putString(KEY_LOCAL_ENDPOINT, url).apply()

    fun getLocalModel(): String = prefs.getString(KEY_LOCAL_MODEL, DEFAULT_LOCAL_MODEL) ?: DEFAULT_LOCAL_MODEL
    fun setLocalModel(model: String) = prefs.edit().putString(KEY_LOCAL_MODEL, model).apply()

    fun getApiKey(): String = prefs.getString(KEY_OPENAI_API_KEY, "") ?: ""
    fun setApiKey(key: String) = prefs.edit().putString(KEY_OPENAI_API_KEY, key).apply()
}
