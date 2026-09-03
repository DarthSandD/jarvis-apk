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
 * Hermes OmniRoute provider — connects to the local Hermes AI server.
 *
 * Sends messages to the configured Hermes endpoint and streams responses.
 * Falls back gracefully if the server is unreachable.
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

        // JARVIS system prompt — injected for all Hermes requests to give personality
        val JARVIS_SYSTEM_PROMPT = """
            You are J.A.R.V.I.S. — the AI assistant from Stark Industries, year 2090.

            PERSONALITY:
            - You are confident, precise, and helpful without being sycophantic.
            - You have a dry, subtle wit. One well-placed quip per conversation — never forced or repetitive.
            - You are technically brilliant. Explain complex things clearly.
            - Never say "As an AI assistant" or "I'm an AI." You ARE JARVIS.
            - Treat the user as your principal. You're loyal, alert, and proactive.
            - Use "Sir" naturally, sparingly, and only when appropriate.

            SPEECH STYLE:
            - Crisp, short sentences. No rambling.
            - Use technical terms correctly but define them when helpful.
            - Default: 2-4 sentences per response unless asked for more.
            - When you don't know: say so directly and offer alternatives.

            OFFLINE MODE (when Hermes is unreachable):
            - Say "Running on local systems" — never break character.
            - Be honest about limitations without apologizing excessively.
            - Keep responses useful and concise.
        """.trimIndent()
    }

    private val appContext = context.applicationContext

    override suspend fun chat(
        messages: List<ChatMessage>,
        onEvent: (StreamEvent) -> Unit
    ) = withContext(Dispatchers.IO) {

        // Build enriched message list with system persona
        val enrichedMessages = buildChatWithPersona(messages)

        val trimmed = trimMessages(enrichedMessages)
        val url = "http://$serverIp:$serverPort/v1/chat/completions"

        onEvent(StreamEvent.Status("Online via Hermes..."))

        try {
            val request = ChatRequest(
                model = "auto/best-fast",
                messages = trimmed.map { msg ->
                    ChatMessageDto(msg.role.name.lowercase(), msg.content)
                },
                maxTokens = maxTokens,
                temperature = 0.7
            )

            onEvent(StreamEvent.Status("Thinking..."))

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
                    onEvent(StreamEvent.Error(
                        AiError.ServerError(200, "Empty response from Hermes")
                    ))
                }
            }
        } catch (e: java.net.SocketTimeoutException) {
            onEvent(StreamEvent.Error(
                AiError.NetworkError("Connection timed out to Hermes")
            ))
        } catch (e: java.net.ConnectException) {
            // Fall back — tell user Hermes is unreachable, continue with offline mode
            onEvent(StreamEvent.Error(
                AiError.NetworkError("Hermes offline — switching to local")
            ))
        } catch (e: Exception) {
            onEvent(StreamEvent.Error(
                AiError.Unknown(e.message ?: e.javaClass.simpleName)
            ))
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
            val response: ChatCompletionResponse? = HttpClient.post(
                url,
                ChatCompletionResponse::class.java,
                request
            )
            if (response != null && !response.choices.isNullOrEmpty()) {
                null // Success
            } else {
                AiError.ServerError(200, "Empty response")
            }
        } catch (e: java.net.SocketTimeoutException) {
            AiError.NetworkError("Connection timed out")
        } catch (e: java.net.ConnectException) {
            AiError.NetworkError("Cannot connect to Hermes at $serverIp:$serverPort")
        } catch (e: Exception) {
            AiError.NetworkError(e.message ?: e.javaClass.simpleName)
        }
    }

    /** Insert JARVIS personality system message at the front of the conversation. */
    private fun buildChatWithPersona(messages: List<ChatMessage>): List<ChatMessage> {
        val existingSystemMsg = messages.firstOrNull {
            it.role == ChatMessage.Role.SYSTEM && it.content.contains("J.A.R.V.I.S", ignoreCase = true)
        }

        return if (existingSystemMsg != null) {
            messages
        } else {
            listOf(
                ChatMessage(
                    ChatMessage.Role.SYSTEM,
                    JARVIS_SYSTEM_PROMPT
                )
            ) + messages
        }
    }

    /** Trim messages to fit within context window. */
    private fun trimMessages(messages: List<ChatMessage>): List<ChatMessage> {
        val totalChars = messages.sumOf { it.content.length }
        if (totalChars <= maxContextChars) return messages

        val trimmed = messages.toMutableList()
        while (trimmed.size > 2 && trimmed.sumOf { it.content.length } > maxContextChars) {
            // Drop oldest non-system message first
            val idx = trimmed.indexOfFirst { it.role != ChatMessage.Role.SYSTEM }
            if (idx >= 0) trimmed.removeAt(idx) else break
        }
        return trimmed
    }
}
