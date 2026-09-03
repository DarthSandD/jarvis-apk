package com.darrenai.jarvis.ai

import com.darrenai.jarvis.model.ChatMessage
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * OpenAI API provider — implements [IProvider] using the official
 * `chat/completions` endpoint with streaming (SSE) enabled.
 *
 * Falls back to a non-streaming request if the stream attempt fails
 * (e.g. proxy strips `stream=true`).
 */
class OpenAiProvider(
    private val apiKey: String,
    private val model: String = PreferencesHelper.DEFAULT_OPENAI_MODEL,
    maxContextChars: Int = 4096,
    override val maxTokens: Int = 512
) : IProvider {

    override val provider: AiProvider get() = AiProvider.OpenAI
    override val maxContextChars: Int = maxContextChars

    companion object {
        private const val ENDPOINT = "https://api.openai.com/v1/chat/completions"
        private const val CONNECT_TIMEOUT_MS = 15_000
        private const val READ_TIMEOUT_MS = 30_000
        private val gson = Gson()
    }

    // ---- DTOs -----------------------------------------------------------

    private data class OpenAiMessage(
        val role: String,
        val content: String
    )

    private data class OpenAiRequest(
        val model: String,
        val messages: List<OpenAiMessage>,
        @SerializedName("max_tokens") val maxTokens: Int,
        val temperature: Double = 0.7,
        val stream: Boolean = true
    )

    private data class StreamChoice(
        val delta: DeltaContent? = null,
        val index: Int = 0,
        @SerializedName("finish_reason") val finishReason: String? = null
    )

    private data class DeltaContent(
        val content: String? = null,
        val role: String? = null
    )

    private data class StreamResponse(
        val id: String? = null,
        val choices: List<StreamChoice>? = null
    )

    private data class NonStreamChoice(
        val message: OpenAiMessage? = null,
        val index: Int = 0,
        @SerializedName("finish_reason") val finishReason: String? = null
    )

    private data class NonStreamResponse(
        val id: String? = null,
        val choices: List<NonStreamChoice>? = null
    )

    // ---- IProvider -------------------------------------------------------

    override suspend fun chat(
        messages: List<ChatMessage>,
        onEvent: (StreamEvent) -> Unit
    ) = withContext(Dispatchers.IO) {

        if (apiKey.isBlank()) {
            onEvent(StreamEvent.Error(AiError.NoApiKey))
            return@withContext
        }

        val trimmed = trimMessages(messages)

        // Attempt streaming first
        val streamed = runCatching { doStream(trimmed, onEvent) }.getOrDefault(false)

        // If streaming didn't produce a result, fall back to non-streaming
        if (!streamed) {
            val full = runCatching { doNonStream(trimmed) }.getOrElse { e ->
                onEvent(StreamEvent.Error(AiError.Unknown(e.message ?: e.javaClass.simpleName)))
                return@withContext
            }
            if (full.isNotEmpty()) {
                onEvent(StreamEvent.Delta(full))
                onEvent(StreamEvent.Done(full))
            } else {
                onEvent(StreamEvent.Error(AiError.Unknown("Empty response from OpenAI")))
            }
        }
    }

    override suspend fun healthCheck(): AiError? = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) return@withContext AiError.NoApiKey

        val body = OpenAiRequest(
            model = model,
            messages = listOf(OpenAiMessage("user", "Say OK")),
            maxTokens = 5,
            stream = false
        )
        try {
            val connection = openConnection()
            writeBody(connection, body)
            val code = connection.responseCode
            when {
                code in 200..299 -> null
                code == 401 -> AiError.NoApiKey
                code == 429 -> AiError.RateLimit
                code in 400..499 -> {
                    val err = readError(connection)
                    AiError.ServerError(code, err)
                }
                else -> {
                    val err = readError(connection)
                    AiError.ServerError(code, err)
                }
            }
        } catch (e: java.net.SocketTimeoutException) {
            AiError.NetworkError("Connection timed out")
        } catch (e: java.net.UnknownHostException) {
            AiError.NetworkError("Cannot resolve api.openai.com")
        } catch (e: Exception) {
            AiError.NetworkError(e.message ?: e.javaClass.simpleName)
        }
    }

    // ---- Streaming -------------------------------------------------------

    private suspend fun doStream(
        messages: List<ChatMessage>,
        onEvent: (StreamEvent) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {

        val body = OpenAiRequest(
            model = model,
            messages = messages.map { OpenAiMessage(it.role.name.lowercase(), it.content) },
            maxTokens = maxTokens,
            stream = true
        )

        val connection = try {
            openConnection()
        } catch (e: Exception) {
            return@withContext false
        }

        return@withContext try {
            writeBody(connection, body)
            val code = connection.responseCode
            if (code !in 200..299) {
                return@withContext false  // Signal fallback to non-streaming
            }

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val fullText = StringBuilder()

            reader.useLines { lines ->
                lines.forEach { raw ->
                    if (raw.startsWith("data: ")) {
                        val data = raw.removePrefix("data: ").trim()
                        if (data == "[DONE]") return@forEach
                        try {
                            val element = JsonParser.parseString(data)
                            if (!element.isJsonObject) return@forEach
                            val obj = element.asJsonObject
                            val choices = obj.getAsJsonArray("choices") ?: return@forEach
                            for (choiceEl in choices) {
                                val choice = choiceEl.asJsonObject
                                val delta = choice.getAsJsonObject("delta") ?: continue
                                val content = delta.get("content") ?: continue
                                if (content.isJsonNull) continue
                                val piece = content.asString
                                fullText.append(piece)
                                onEvent(StreamEvent.Delta(piece))
                            }
                        } catch (_: Exception) {
                            // Skip malformed SSE frames
                        }
                    }
                }
            }

            if (fullText.isNotEmpty()) {
                onEvent(StreamEvent.Done(fullText.toString()))
                true
            } else {
                false  // Empty stream — signal fallback
            }
        } catch (e: Exception) {
            false  // Signal fallback
        }
    }

    // ---- Non-streaming ---------------------------------------------------

    private suspend fun doNonStream(messages: List<ChatMessage>): String = withContext(Dispatchers.IO) {
        val body = OpenAiRequest(
            model = model,
            messages = messages.map { OpenAiMessage(it.role.name.lowercase(), it.content) },
            maxTokens = maxTokens,
            stream = false
        )

        val connection = openConnection()
        try {
            writeBody(connection, body)
            val code = connection.responseCode
            if (code in 200..299) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val text = reader.readText()
                reader.close()
                val resp = gson.fromJson(text, NonStreamResponse::class.java)
                resp.choices?.firstOrNull()?.message?.content ?: ""
            } else {
                val err = readError(connection)
                when (code) {
                    401 -> throw RuntimeException("Invalid API key")
                    429 -> throw RuntimeException("Rate limited")
                    in 400..499 -> throw RuntimeException("OpenAI error $code: $err")
                    else -> throw RuntimeException("OpenAI error $code: $err")
                }
            }
        } finally {
            connection.disconnect()
        }
    }

    // ---- Helpers ----------------------------------------------------------

    private fun openConnection(): HttpURLConnection {
        val url = URL(ENDPOINT)
        return (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Authorization", "Bearer $apiKey")
            doOutput = true
            connectTimeout = CONNECT_TIMEOUT_MS
            readTimeout = READ_TIMEOUT_MS
        }
    }

    private fun writeBody(connection: HttpURLConnection, body: Any) {
        val json = gson.toJson(body)
        connection.outputStream.use { it.write(json.toByteArray(Charsets.UTF_8)) }
    }

    private fun readError(connection: HttpURLConnection): String {
        return try {
            val stream = connection.errorStream ?: return ""
            val text = stream.bufferedReader().readText()
            stream.close()
            text.take(200)
        } catch (_: Exception) {
            ""
        }
    }

    /** Drop oldest messages until the estimated character budget fits. */
    private fun trimMessages(messages: List<ChatMessage>): List<ChatMessage> {
        val totalChars = messages.sumOf { it.content.length }
        if (totalChars <= maxContextChars) return messages

        val trimmed = messages.toMutableList()
        while (trimmed.size > 2 && trimmed.sumOf { it.content.length } > maxContextChars) {
            // Drop the oldest non-system message
            val idx = trimmed.indexOfFirst { it.role != ChatMessage.Role.SYSTEM }
            if (idx >= 0) trimmed.removeAt(idx) else break
        }
        return trimmed
    }
}