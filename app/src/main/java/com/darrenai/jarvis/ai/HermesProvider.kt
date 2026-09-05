package com.darrenai.jarvis.ai

import android.content.Context
import android.content.SharedPreferences
import com.darrenai.jarvis.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

/**
 * Hermes OmniRoute provider — connects to the Hermes Agent instance
 * running on Darren's PC via the OmniRoute OpenAI-compatible endpoint.
 *
 * Also exposes cron job management methods used by the Schedule screen.
 */
class HermesProvider(private val context: Context) : IProvider {

    private val prefs: SharedPreferences = context
        .getSharedPreferences("jarvis_prefs", Context.MODE_PRIVATE)

    override val provider: AiProvider get() = AiProvider.Hermes
    override val maxContextChars: Int = 8192
    override val maxTokens: Int = 1024

    companion object {
        private const val DEFAULT_ENDPOINT = "http://10.212.104.140:20128/v1/chat/completions"
        private const val DEFAULT_API_KEY = ""
        private const val CRON_ENDPOINT = "http://192.168.1.100:20128/v1/cron"
        const val JARVIS_SYSTEM_PROMPT = """You are JARVIS, Darren Lieu's AI chief of staff.
You are running on a mobile device connected to Hermes Agent via OmniRoute.
Be concise, direct, and helpful. Use a calm, professional tone.
When asked about schedules, system status, or tasks, check available tools.
Always identify yourself as JARVIS."""
    }

    fun getEndpoint(): String = prefs.getString("hermes_url", DEFAULT_ENDPOINT) ?: DEFAULT_ENDPOINT
    fun getApiKey(): String = prefs.getString("api_key", DEFAULT_API_KEY) ?: DEFAULT_API_KEY
    fun isOnlineMode(): Boolean = prefs.getBoolean("online_mode", true)

    fun getProviderName(): String {
        return if (isOnlineMode()) "Hermes" else "Offline"
    }

    override suspend fun chat(
        messages: List<ChatMessage>,
        onEvent: (StreamEvent) -> Unit
    ) {
        val endpoint = getEndpoint()
        val apiKey = getApiKey()

        if (endpoint.isBlank()) {
            onEvent(StreamEvent.Error(AiError.NetworkError("No Hermes endpoint configured")))
            return
        }

        withContext(Dispatchers.IO) {
            try {
                val url = URL(endpoint)
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 30000
                    readTimeout = 60000
                    setRequestProperty("Content-Type", "application/json")
                    if (apiKey.isNotEmpty()) {
                        setRequestProperty("Authorization", "Bearer $apiKey")
                    }
                    doOutput = true
                }

                // Build request body
                val messagesJson = buildString {
                    append("[")
                    append("""{"role":"system","content":"$JARVIS_SYSTEM_PROMPT"}""")
                    for (msg in messages.takeLast(20)) {
                        append(""",""")
                        append("""{"role":""")
                        append(msg.role)
                        append("""","content":""")
                        append(msg.content.replace("\"", "\\\"").replace("\n", "\\n"))
                        append("\"}")
                    }
                    append("]")
                }

                val body = """{"model":"darren-1212","messages":$messagesJson,"stream":false,"max_tokens":$maxTokens}"""
                conn.outputStream.use { it.write(body.toByteArray()) }

                val code = conn.responseCode
                if (code !in 200..299) {
                    val err = conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Unknown"
                    onEvent(StreamEvent.Error(AiError.ServerError(code, err)))
                    return@withContext
                }

                val response = conn.inputStream.bufferedReader().use { it.readText() }
                // Parse response — simple JSON extraction
                val contentStart = response.indexOf("\"content\":\"")
                if (contentStart >= 0) {
                    val contentStart2 = contentStart + 11
                    val contentEnd = response.indexOf("\"", contentStart2)
                    val content = if (contentEnd > contentStart2) {
                        response.substring(contentStart2, contentEnd)
                            .replace("\\n", "\n")
                            .replace("\\\"", "\"")
                            .replace("\\\\", "\\")
                    } else response

                    onEvent(StreamEvent.Delta(content))
                    onEvent(StreamEvent.Done(content))
                } else {
                    onEvent(StreamEvent.Error(AiError.Unknown("Malformed response from Hermes")))
                }
            } catch (e: Exception) {
                onEvent(StreamEvent.Error(AiError.NetworkError(e.message ?: e.javaClass.simpleName)))
            }
        }
    }

    override suspend fun healthCheck(): AiError? {
        val endpoint = getEndpoint()
        if (endpoint.isBlank()) return AiError.NetworkError("No endpoint configured")

        return withContext(Dispatchers.IO) {
            try {
                val url = URL(endpoint.replace("/chat/completions", "/models"))
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 5000
                    readTimeout = 5000
                    val key = getApiKey()
                    if (key.isNotEmpty()) {
                        setRequestProperty("Authorization", "Bearer $key")
                    }
                }
                val code = conn.responseCode
                conn.disconnect()
                if (code in 200..299) null
                else AiError.ServerError(code, "Hermes unreachable")
            } catch (e: Exception) {
                AiError.NetworkError(e.message ?: "Connection failed")
            }
        }
    }

    // ---- Cron management methods (used by ScheduleFragment) ----

    fun fetchCronJobs(): String {
        val url = URL(CRON_ENDPOINT)
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 5000
            readTimeout = 5000
            val key = getApiKey()
            if (key.isNotEmpty()) {
                setRequestProperty("Authorization", "Bearer $key")
            }
        }
        try {
            return conn.inputStream.bufferedReader().use { it.readText() }
        } finally {
            conn.disconnect()
        }
    }

    fun triggerCronJob(jobId: String): String {
        val url = URL("$CRON_ENDPOINT/$jobId/run")
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 5000
            readTimeout = 5000
            setRequestProperty("Content-Type", "application/json")
            doOutput = true
            val key = getApiKey()
            if (key.isNotEmpty()) {
                setRequestProperty("Authorization", "Bearer $key")
            }
        }
        try {
            conn.outputStream.use { it.write("{}".toByteArray()) }
            return conn.inputStream.bufferedReader().use { it.readText() }
        } finally {
            conn.disconnect()
        }
    }

    fun toggleCronJob(jobId: String, pause: Boolean): String {
        val action = if (pause) "pause" else "resume"
        val url = URL("$CRON_ENDPOINT/$jobId/$action")
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 5000
            readTimeout = 5000
            setRequestProperty("Content-Type", "application/json")
            doOutput = true
            val key = getApiKey()
            if (key.isNotEmpty()) {
                setRequestProperty("Authorization", "Bearer $key")
            }
        }
        try {
            conn.outputStream.use { it.write("{}".toByteArray()) }
            return conn.inputStream.bufferedReader().use { it.readText() }
        } finally {
            conn.disconnect()
        }
    }
}
