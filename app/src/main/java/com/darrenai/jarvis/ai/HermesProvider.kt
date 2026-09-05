package com.darrenai.jarvis.ai

import android.content.Context
import androidx.preference.PreferenceManager
import com.darrenai.jarvis.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Hermes OmniRoute provider — connects to the Hermes Agent instance
 * via the OmniRoute OpenAI-compatible endpoint.
 *
 * Self-contained routing: defaults to the PC on the LAN
 * (http://10.212.104.124:20128). Only internet/LAN access is needed.
 * Reads the endpoint from the same SharedPreferences file the Settings
 * screen writes (default shared prefs), with legacy fallback.
 *
 * Also exposes cron job management methods used by the Schedule screen.
 */
class HermesProvider(private val context: Context) : IProvider {

    private val defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val legacyPrefs = context
        .getSharedPreferences("jarvis_prefs", Context.MODE_PRIVATE)

    override val provider: AiProvider get() = AiProvider.Hermes
    override val maxContextChars: Int = 8192
    override val maxTokens: Int = 1024

    companion object {
        const val DEFAULT_ENDPOINT = "http://10.212.104.124:20128/v1/chat/completions"
        private const val DEFAULT_API_KEY = ""
        const val JARVIS_SYSTEM_PROMPT = """You are JARVIS, Darren Lieu's AI chief of staff.
You are running on a mobile device connected to Hermes Agent via OmniRoute.
Be concise, direct, and helpful. Use a calm, professional tone.
When asked about schedules, system status, or tasks, check available tools.
Always identify yourself as JARVIS."""

        fun roleName(role: ChatMessage.Role): String = when (role) {
            ChatMessage.Role.USER -> "user"
            ChatMessage.Role.ASSISTANT -> "assistant"
            ChatMessage.Role.SYSTEM -> "system"
        }

        fun escapeJson(s: String): String {
            val sb = StringBuilder(s.length + 16)
            for (c in s) {
                when (c) {
                    '\\' -> sb.append("\\\\")
                    '"' -> sb.append("\\\"")
                    '\n' -> sb.append("\\n")
                    '\r' -> sb.append("\\r")
                    '\t' -> sb.append("\\t")
                    else -> if (c < ' ') sb.append(String.format("\\u%04x", c.code)) else sb.append(c)
                }
            }
            return sb.toString()
        }

        /** Extract assistant content from an OpenAI-compatible chat response. */
        fun parseContent(response: String): String? {
            return try {
                val root = JSONObject(response)
                val choices = root.optJSONArray("choices") ?: return null
                if (choices.length() == 0) return null
                val msg = choices.getJSONObject(0).optJSONObject("message") ?: return null
                val content = msg.optString("content", null)?.takeIf { it.isNotEmpty() }
                content
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getEndpoint(): String {
        val v = defaultPrefs.getString("hermes_url", null)
            ?: legacyPrefs.getString("hermes_url", null)
            ?: DEFAULT_ENDPOINT
        return v.ifBlank { DEFAULT_ENDPOINT }
    }

    fun getApiKey(): String {
        val v = defaultPrefs.getString("api_key", null)
            ?: legacyPrefs.getString("api_key", null)
            ?: DEFAULT_API_KEY
        return v
    }

    fun isOnlineMode(): Boolean = defaultPrefs.getBoolean("online_mode", true)

    fun getProviderName(): String {
        return if (isOnlineMode()) "Hermes" else "Offline"
    }

    private fun cronBase(): String {
        // Derive cron base from the chat endpoint host, e.g.
        // http://10.212.104.124:20128/v1/chat/completions -> http://10.212.104.124:20128/v1/cron
        return try {
            val u = URL(getEndpoint())
            val port = if (u.port == -1) "" else ":${u.port}"
            "${u.protocol}://${u.host}$port/v1/cron"
        } catch (e: Exception) {
            "http://10.212.104.124:20128/v1/cron"
        }
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
                    append("{\"role\":\"system\",\"content\":\"${escapeJson(JARVIS_SYSTEM_PROMPT)}\"}")
                    for (msg in messages.takeLast(20)) {
                        append(",")
                        append("{\"role\":\"")
                        append(roleName(msg.role))
                        append("\",\"content\":\"")
                        append(escapeJson(msg.content))
                        append("\"}")
                    }
                    append("]")
                }

                val body = "{\"model\":\"darren-1212\",\"messages\":$messagesJson,\"stream\":false,\"max_tokens\":$maxTokens}"
                conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }

                val code = conn.responseCode
                if (code !in 200..299) {
                    val err = try {
                        conn.errorStream?.bufferedReader()?.use { it.readText() } ?: "Unknown"
                    } catch (e: Exception) {
                        "Unknown"
                    }
                    onEvent(StreamEvent.Error(AiError.ServerError(code, err.take(300))))
                    return@withContext
                }

                val response = conn.inputStream.bufferedReader().use { it.readText() }
                val content = parseContent(response)
                if (content != null) {
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
        val url = URL(cronBase())
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
        val url = URL("${cronBase()}/$jobId/run")
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
        val url = URL("${cronBase()}/$jobId/$action")
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
