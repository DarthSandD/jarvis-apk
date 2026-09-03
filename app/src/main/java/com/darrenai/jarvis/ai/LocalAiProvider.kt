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
 * Local AI provider — communicates with a [llama.cpp](https://github.com/ggerganov/llama.cpp)
 * server that exposes the OpenAI-compatible `/v1/chat/completions` endpoint.
 *
 * The endpoint URL and model name are fully configurable via [PreferencesHelper].
 */
class LocalAiProvider(
    private val endpoint: String = PreferencesHelper.DEFAULT_LOCAL_ENDPOINT,
    private val model: String = PreferencesHelper.DEFAULT_LOCAL_MODEL,
    maxContextChars: Int = 4096,
    override val maxTokens: Int = 512
) : IProvider {

    override val provider: AiProvider get() = AiProvider.Local
    override val maxContextChars: Int = maxContextChars

    companion object {
        private const val CONNECT_TIMEOUT_MS = 10_000
        private const val READ_TIMEOUT_MS = 60_000
        private val gson = Gson()

        /** Normalize an endpoint: strip trailing slashes and ensure /chat/completions suffix. */
        fun normalizeEndpoint(raw: String): String {
            var url = raw.trimEnd('/')
            if (!url.endsWith("/v1")) url += "/v1"
            return url
        }
    }

    // ---- DTOs -----------------------------------------------------------

    private data class LlamaMessage(val role: String, val content: String)

    private data class LlamaRequest(
        val model: String,
        val messages: List<LlamaMessage>,
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
        val message: LlamaMessage? = null,
        val index: Int = 0,
        @SerializedName("finish_reason") val finishReason: String? = null
    )

    private data class NonStreamResponse(
        val id: String? = null,
        val choices: List<NonStreamChoice>? = null
    )

    private data class ModelsResponse(
        val data: List<ModelInfo>? = null
    )

    private data class ModelInfo(
        val id: String
    )

    // ---- IProvider -------------------------------------------------------

    override suspend fun chat(
        messages: List<ChatMessage>,
        onEvent: (StreamEvent) -> Unit
    ) = withContext(Dispatchers.IO) {

        val url = normalizeEndpoint(endpoint)
        val completionsUrl = "$url/chat/completions"

        val trimmed = trimMessages(messages)

        // Attempt streaming first
        val streamed = runCatching { doStream(completionsUrl, trimmed, onEvent) }.getOrDefault(false)
        if (streamed) return@withContext

        // Fallback to non-streaming
        val full = runCatching { doNonStream(completionsUrl, trimmed) }.getOrElse { e ->
            onEvent(StreamEvent.Error(AiError.Unknown(e.message ?: e.javaClass.simpleName)))
            return@withContext
        }
        if (full.isNotEmpty()) {
            onEvent(StreamEvent.Delta(full))
            onEvent(StreamEvent.Done(full))
        } else {
            onEvent(StreamEvent.Error(AiError.Unknown("Empty response from local server")))
        }
    }

    override suspend fun healthCheck(): AiError? = withContext(Dispatchers.IO) {
        val url = normalizeEndpoint(endpoint)
        val completionsUrl = "$url/chat/completions"

        val body = LlamaRequest(
            model = model,
            messages = listOf(LlamaMessage("user", "Say OK")),
            maxTokens = 5,
            stream = false
        )
        try {
            val connection = openConnection(completionsUrl)
            writeBody(connection, body)
            val code = connection.responseCode
            when {
                code in 200..299 -> null
                code == 429 -> AiError.RateLimit
                code in 400..499 -> AiError.ServerError(code, "Bad request")
                else -> AiError.ServerError(code, "Server error")
            }
        } catch (e: java.net.SocketTimeoutException) {
            AiError.NetworkError("Connection timed out")
        } catch (e: java.net.ConnectException) {
            AiError.NetworkError("Cannot connect to $url — is the server running?")
        } catch (e: Exception) {
            AiError.NetworkError(e.message ?: e.javaClass.simpleName)
        }
    }

    /**
     * Auto-detect available models by querying `/v1/models`.
     * Returns a list of model IDs, or empty list if the endpoint is unreachable.
     */
    suspend fun detectModels(): List<String> = withContext(Dispatchers.IO) {
        val url = normalizeEndpoint(endpoint)
        val modelsUrl = "$url/models"
        try {
            val connection = openConnection(modelsUrl)
            connection.requestMethod = "GET"
            val code = connection.responseCode
            if (code in 200..299) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val text = reader.readText()
                reader.close()
                connection.disconnect()
                val resp = gson.fromJson(text, ModelsResponse::class.java)
                resp.data?.map { it.id } ?: emptyList()
            } else {
                connection.disconnect()
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    // ---- Streaming -------------------------------------------------------

    private suspend fun doStream(
        completionsUrl: String,
        messages: List<ChatMessage>,
        onEvent: (StreamEvent) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {

        val body = LlamaRequest(
            model = model,
            messages = messages.map { LlamaMessage(it.role.name.lowercase(), it.content) },
            maxTokens = maxTokens,
            stream = true
        )

        val connection = try {
            openConnection(completionsUrl)
        } catch (e: Exception) {
            return@withContext false
        }

        return@withContext try {
            writeBody(connection, body)
            val code = connection.responseCode
            if (code !in 200..299) {
                return@withContext false
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
                            // Skip malformed frames
                        }
                    }
                }
            }

            if (fullText.isNotEmpty()) {
                onEvent(StreamEvent.Done(fullText.toString()))
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    // ---- Non-streaming ---------------------------------------------------

    private suspend fun doNonStream(
        completionsUrl: String,
        messages: List<ChatMessage>
    ): String = withContext(Dispatchers.IO) {
        val body = LlamaRequest(
            model = model,
            messages = messages.map { LlamaMessage(it.role.name.lowercase(), it.content) },
            maxTokens = maxTokens,
            stream = false
        )

        val connection = openConnection(completionsUrl)
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
                throw RuntimeException("Local server error $code")
            }
        } finally {
            connection.disconnect()
        }
    }

    // ---- Helpers ----------------------------------------------------------

    private fun openConnection(url: String): HttpURLConnection {
        val urlObj = URL(url)
        return (urlObj.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
            doOutput = true
            connectTimeout = CONNECT_TIMEOUT_MS
            readTimeout = READ_TIMEOUT_MS
        }
    }

    private fun writeBody(connection: HttpURLConnection, body: Any) {
        val json = gson.toJson(body)
        connection.outputStream.use { it.write(json.toByteArray(Charsets.UTF_8)) }
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