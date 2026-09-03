package com.darrenai.jarvis.ai

import android.content.Context
import com.darrenai.jarvis.model.ChatMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Unified AI service that exposes a single entry point to all providers.
 *
 * Responsibilities:
 *  • Maintains a registry of available providers.
 *  • Applies the fallback chain: selected → Hermes → Local → error.
 *  • Streams partial responses to the UI via callback.
 *  • Emits typed errors through [StreamEvent.Error].
 */
class AiService private constructor(
    private val context: Context
) {
    /** Coroutine scope tied to the lifecycle of the service. */
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    /** User preferences (provider selection, API keys, endpoints). */
    private val prefs = PreferencesHelper(context)

    /** Cache of provider instances, keyed by [AiProvider.id]. */
    private val providers = mutableMapOf<String, IProvider>()

    init {
        // Pre-warm providers that don't need to be rebuilt on every call
        providers[AiProvider.Hermes.id] = HermesProvider(context)
    }

    /**
     * Send a chat message and receive streaming responses.
     *
     * @param messages The full conversation history.
     * @param preferredProvider The user's chosen provider, or null to fall back to saved preference.
     * @param onEvent Callback invoked on the main thread for each streaming event.
     */
    fun chat(
        messages: List<ChatMessage>,
        preferredProvider: AiProvider? = null,
        onEvent: (StreamEvent) -> Unit
    ) {
        val target = preferredProvider ?: prefs.selectedProvider
        scope.launch {
            val chain = buildFallbackChain(target)
            var lastError: AiError? = null

            for (provider in chain) {
                val result = runProviderSafely(provider, messages, onEvent)
                when (result) {
                    ProviderResult.Success -> return@launch
                    is ProviderResult.PartialError -> {
                        lastError = result.error
                        onEvent(StreamEvent.Status("Provider ${provider.provider.displayName} failed: ${result.error.message}"))
                        // Continue to next provider in chain
                    }
                    is ProviderResult.CompleteError -> {
                        lastError = result.error
                        // Continue to next provider
                    }
                }
            }

            // All providers exhausted
            onEvent(StreamEvent.Error(lastError ?: AiError.Unknown("No AI provider available")))
        }
    }

    /**
     * Run a connectivity check for a specific provider.
     */
    suspend fun healthCheck(provider: AiProvider): AiError? {
        val p = getOrCreateProvider(provider)
        return p.healthCheck()
    }

    /**
     * Auto-detect available models on the local server.
     */
    suspend fun detectLocalModels(endpoint: String = prefs.localEndpoint): List<String> {
        val localProvider = LocalAiProvider(endpoint = endpoint, model = prefs.localModel)
        return localProvider.detectModels()
    }

    /**
     * List providers that are currently usable (configured).
     */
    fun availableProviders(): List<AiProvider> {
        val list = mutableListOf<AiProvider>()

        if (prefs.openAiApiKey.isNotBlank()) list.add(AiProvider.OpenAI)
        // Local is always listed — user may configure it after first launch
        list.add(AiProvider.Local)
        // Hermes is always listed
        list.add(AiProvider.Hermes)

        return list.distinct()
    }

    /** Cancel in-flight coroutines (call from onDestroy). */
    fun destroy() {
        scope.cancel()
    }

    // ---- Internal ---------------------------------------------------------

    private sealed class ProviderResult {
        data object Success : ProviderResult()
        data class PartialError(val error: AiError) : ProviderResult()
        data class CompleteError(val error: AiError) : ProviderResult()
    }

    private suspend fun runProviderSafely(
        provider: IProvider,
        messages: List<ChatMessage>,
        onEvent: (StreamEvent) -> Unit
    ): ProviderResult {
        return try {
            var sawDone = false
            var sawError = false
            var lastError: AiError? = null

            provider.chat(messages) { event ->
                when (event) {
                    is StreamEvent.Delta -> {
                        // Forward to UI
                    }
                    is StreamEvent.Status -> {
                        // Forward to UI as progress
                    }
                    is StreamEvent.Done -> {
                        sawDone = true
                    }
                    is StreamEvent.Error -> {
                        sawError = true
                        lastError = event.error
                    }
                }
                onEvent(event)
            }

            when {
                sawDone -> ProviderResult.Success
                sawError -> ProviderResult.PartialError(lastError ?: AiError.Unknown("Unknown error"))
                else -> ProviderResult.CompleteError(AiError.Unknown("Provider returned without result"))
            }
        } catch (e: Exception) {
            ProviderResult.CompleteError(AiError.Unknown(e.message ?: e.javaClass.simpleName))
        }
    }

    /**
     * Build the fallback chain. First entry is the selected provider,
     * then Hermes, then Local (if different from first two).
     */
    private fun buildFallbackChain(selected: AiProvider): List<IProvider> {
        val chain = mutableListOf<IProvider>()

        // 1. Selected provider
        chain.add(getOrCreateProvider(selected))

        // 2. Hermes (if not already selected)
        if (selected != AiProvider.Hermes) {
            chain.add(getOrCreateProvider(AiProvider.Hermes))
        }

        // 3. Local (if not already in chain)
        if (selected != AiProvider.Local) {
            chain.add(getOrCreateProvider(AiProvider.Local))
        }

        return chain.distinctBy { it.provider.id }
    }

    /**
     * Get an existing provider instance from the cache, or create a new one.
     */
    private fun getOrCreateProvider(provider: AiProvider): IProvider {
        return providers.getOrPut(provider.id) {
            when (provider) {
                AiProvider.OpenAI -> OpenAiProvider(
                    apiKey = prefs.openAiApiKey,
                    model = prefs.openAiModel
                )
                AiProvider.Local -> LocalAiProvider(
                    endpoint = prefs.localEndpoint,
                    model = prefs.localModel
                )
                AiProvider.Hermes -> HermesProvider(context)
            }
        }
    }

    // ---- Singleton --------------------------------------------------------

    companion object {
        @Volatile private var instance: AiService? = null

        fun getInstance(context: Context): AiService {
            return instance ?: synchronized(this) {
                instance ?: AiService(context.applicationContext).also { instance = it }
            }
        }
    }
}