package com.darrenai.jarvis.ai

import android.content.Context
import com.darrenai.jarvis.ConnectivityManager
import com.darrenai.jarvis.model.ChatMessage
import com.darrenai.jarvis.network.ChatCompletionResponse
import com.darrenai.jarvis.network.ChatRequest
import com.darrenai.jarvis.network.ChatMessageDto
import com.darrenai.jarvis.network.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Hermes OmniRoute provider — wraps the existing [ConnectivityManager]
 * implementation so it conforms to the unified [IProvider] interface.
 *
 * Uses the current Hermes backend at the configured IP/port.
 */
class HermesProvider(
    context: Context,
    private val serverIp: String = ConnectivityManager.serverIp,
    private val serverPort: Int = ConnectivityManager.serverPort,
    maxTokens: Int = ConnectivityManager.MAX_TOKENS,
    maxContextChars: Int = ConnectivityManager.MAX_CONTEXT
) : IProvider {

    override val provider: AiProvider get() = AiProvider.Hermes
    override val maxContextChars: Int = maxContextChars
    override val maxTokens: Int = maxTokens

    companion object {
        private const val CONNECT_TIMEOUT_MS = 15_000
        private const val READ_TIMEOUT_MS = 30_000
    }

    private val appContext = context.applicationContext

    override suspend fun chat(
        messages: List<ChatMessage>,
        onEvent: (StreamEvent) -> Unit
    ) = withContext(Dispatchers.IO) {

        val trimmed = trimMessages(messages)
        val url = "http://$serverIp:$serverPort/v1/chat/completions"

        onEvent(StreamEvent.Status("Connecting to Hermes..."))

        try {
            val request = ChatRequest(
                model = "auto/best-fast",
                messages = trimmed.map {
                    ChatMessageDto(it.role.name.lowercase(), it.content)
                },
                maxTokens = maxTokens,
                temperature = 0.7
            )

            onEvent(StreamEvent.Status("Waiting for response..."))

            val response: ChatCompletionResponse? = coroutineScope {
                async {
                    HttpClient.post(url, ChatCompletionResponse::class.java, request)
                }.await()
            }

            when {
                response != null && !response.choices.isNullOrEmpty() -> {
                    val answer = response.choices[0].message?.content ?: "No response"
                    onEvent(StreamEvent.Delta(answer))
                    onEvent(StreamEvent.Done(answer))
                }
                else -> {
                    onEvent(StreamEvent.Error(AiError.ServerError(200, "Empty response from Hermes")))
                }
            }
        } catch (e: java.net.SocketTimeoutException) {
            onEvent(StreamEvent.Error(AiError.NetworkError("Connection timed out")))
        } catch (e: java.net.ConnectException) {
            onEvent(StreamEvent.Error(AiError.NetworkError("Cannot connect to Hermes at $serverIp:$serverPort")))
        } catch (e: Exception) {
            onEvent(StreamEvent.Error(AiError.Unknown(e.message ?: e.javaClass.simpleName)))
        }
    }

    override suspend fun healthCheck(): AiError? = withContext(Dispatchers.IO) {
        val url = "http://$serverIp:$serverPort/v1/chat/completions"
        try {
            val request = ChatRequest(
                model = "auto/best-fast",
                messages = listOf(ChatMessageDto("user", "Say OK")),
                maxTokens = 5,
                temperature = 0.7
            )
            val response: ChatCompletionResponse? = HttpClient.post(url, ChatCompletionResponse::class.java, request)
            if (response != null && !response.choices.isNullOrEmpty()) {
                null  // Success
            } else {
                AiError.ServerError(200, "Empty response")
            }
        } catch (e: java.net.SocketTimeoutException) {
            AiError.NetworkError("Connection timed out")
        } catch (e: java.net.ConnectException) {
            AiError.NetworkError("Cannot connect to $serverIp:$serverPort")
        } catch (e: Exception) {
            AiError.NetworkError(e.message ?: e.javaClass.simpleName)
        }
    }

    private fun trimMessages(messages: List<ChatMessage>): List<ChatMessage> {
        val totalChars = messages.sumOf { it.content.length }
        if (totalChars <= maxContextChars) return messages

        val trimmed = messages.toMutableList()
        while (trimmed.size > 2 && trimmed.sumOf { it.content.length } > maxContextChars) {
            val idx = trimmed.indexOfFirst { it.role != ChatMessage.Role.SYSTEM }
            if (idx >= 0) trimmed.removeAt(idx) else break
        }
        return trimmed
    }
}