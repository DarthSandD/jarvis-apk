package com.darrenai.jarvis.ai

/**
 * Sealed class representing the three AI provider options in JARVIS.
 *
 * Each provider has a stable [id] (used for persistence / settings) and a
 * user-facing [displayName].
 */
sealed class AiProvider(val id: String, val displayName: String) {
    data object OpenAI : AiProvider("openai", "OpenAI API")
    data object Local : AiProvider("local", "Local AI (llama.cpp)")
    data object Hermes : AiProvider("hermes", "Hermes OmniRoute")

    companion object {
        /** Look up a sealed instance by its stored id, defaulting to Hermes. */
        fun fromId(id: String?): AiProvider = when (id) {
            OpenAI.id -> OpenAI
            Local.id -> Local
            Hermes.id -> Hermes
            else -> Hermes
        }

        /** All providers, in display order. */
        fun all(): List<AiProvider> = listOf(OpenAI, Local, Hermes)
    }
}