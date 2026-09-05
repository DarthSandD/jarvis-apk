package com.darrenai.jarvis.network

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

data class ChatMessageDto(
    val role: String,
    val content: String
)

data class ChatRequest(
    val model: String,
    val messages: List<ChatMessageDto>,
    @SerializedName("max_tokens") val maxTokens: Int = 512,
    val temperature: Double = 0.7
)

data class ChatChoice(
    val message: ChatMessageDto? = null,
    val index: Int = 0
)

data class ChatCompletionResponse(
    val id: String? = null,
    val choices: List<ChatChoice>? = null
)

object HttpClient {
    private val gson = Gson()

    fun <T> post(
        url: String,
        responseClass: Class<T>,
        body: Any,
        connectTimeoutMs: Int = 15000,
        readTimeoutMs: Int = 30000
    ): T? {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
            doOutput = true
            connectTimeout = connectTimeoutMs
            readTimeout = readTimeoutMs
        }
        return try {
            val jsonBody = gson.toJson(body)
            connection.outputStream.use { it.write(jsonBody.toByteArray(Charsets.UTF_8)) }
            val responseCode = connection.responseCode
            if (responseCode in 200..299) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val responseText = reader.readText()
                reader.close()
                gson.fromJson(responseText, responseClass)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            connection.disconnect()
        }
    }
}
