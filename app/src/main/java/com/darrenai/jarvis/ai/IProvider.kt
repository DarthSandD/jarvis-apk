package com.darrenai.jarvis.ai

import com.darrenai.jarvis.model.ChatMessage

/**
 * Error types returned by any AI provider when a chat completion fails.
 */
sealed class AiError(val message: String) {
    data object NoApiKey : AiError("No API key configured. Please add an OpenAI API key in Settings.")
    data class NetworkError(val detail: String) : AiError("Network error: $detail")
    data class ServerError(val code: Int, val detail: String) : AiError("Server error $code: $detail")
    data object RateLimit : AiError("Rate limited. Please wait and try again.")
    data class Unknown(val detail: String) : AiError("Unexpected error: $detail")
}

/**
 * Represents a streaming chunk from a provider.
 */
sealed class StreamEvent {
    /** A piece of the assistant's response text (becomes part of the final answer). */
    data class Delta(val text: String) : StreamEvent()
    /** A status message (progress indicator, NOT part of the final answer). */
    data class Status(val message: String) : StreamEvent()
    /** The stream completed successfully with the full text. */
    data class Done(val fullText: String) : StreamEvent()
    /** An error occurred mid-stream. */
    data class Error(val error: AiError) : StreamEvent()
}

/**
 * Common interface that every AI provider must implement.
 *
 * All calls are suspend functions so callers can choose their coroutine scope.
 */
interface IProvider {
    /** Provider metadata (id + display name). */
    val provider: AiProvider

    /** Max characters of history to send to the model. */
    val maxContextChars: Int get() = 4096

    /** Max tokens in the response. */
    val maxTokens: Int get() = 512

    /**
     * Send [messages] to the AI and receive streaming chunks via [onEvent].
     *
     * Implementations should handle timeouts internally (30 s for OpenAI,
     * 60 s for local) and emit [StreamEvent.Error] rather than throwing.
     */
    suspend fun chat(
        messages: List<ChatMessage>,
        onEvent: (StreamEvent) -> Unit
    )

    /**
     * Quick connectivity check — used for "Test Connection" buttons.
     * Returns null on success, an [AiError] on failure.
     */
    suspend fun healthCheck(): AiError?
}