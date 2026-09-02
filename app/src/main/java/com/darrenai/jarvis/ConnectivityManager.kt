package com.darrenai.jarvis

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager as NetConnManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.darrenai.jarvis.model.ChatMessage

class ConnectivityManager(private val context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("jarvis_settings", Context.MODE_PRIVATE)
    }

    init {
        _prefs = prefs
    }

    companion object {
        @Volatile private var _prefs: SharedPreferences? = null
        
        var serverIp: String
            get() = _prefs?.getString("server_ip", "10.212.104.140") ?: "10.212.104.140"
            set(value) { val p = _prefs; if (p != null) { p.edit().putString("server_ip", value); } }

        var serverPort: Int
            get() = _prefs?.getInt("server_port", 20128) ?: 20128
            set(value) { val p = _prefs; if (p != null) { p.edit().putInt("server_port", value); } }

        const val MAX_CONTEXT = 4096
        const val MAX_TOKENS = 512
    }

    enum class Mode { ONLINE, OFFLINE, UNKNOWN }

    var listener: ((Mode) -> Unit)? = null

    private val networkCallback = object : NetConnManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            listener?.invoke(Mode.ONLINE)
        }
        override fun onLost(network: Network) {
            listener?.invoke(Mode.OFFLINE)
        }
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            val hasInternet = networkCapabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )
            if (hasInternet) {
                listener?.invoke(Mode.ONLINE)
            } else {
                listener?.invoke(Mode.OFFLINE)
            }
        }
    }

    private val connectivityManager: NetConnManager by lazy {
        context.getSystemService(NetConnManager::class.java)
    }

    fun startMonitoring() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    fun stopMonitoring() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun getCurrentMode(): Mode {
        val cm = context.getSystemService(NetConnManager::class.java)
        val activeNetwork = cm?.activeNetwork ?: return Mode.OFFLINE
        val caps = cm.getNetworkCapabilities(activeNetwork) ?: return Mode.OFFLINE
        return when {
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> Mode.ONLINE
            else -> Mode.OFFLINE
        }
    }

    fun isOnline(): Boolean = getCurrentMode() == Mode.ONLINE

    suspend fun sendMessage(
        messages: List<ChatMessage>,
        mode: Mode,
        onProgress: (String) -> Unit = {}
    ): String {
        return when (mode) {
            Mode.ONLINE -> {
                try {
                    val url = "http://${serverIp}:${serverPort}/v1/chat/completions"
                    val body = com.darrenai.jarvis.network.ChatRequest(
                        model = "auto/best-fast",
                        messages = messages.map { com.darrenai.jarvis.network.ChatMessageDto(it.role.name.lowercase(), it.content) },
                        maxTokens = MAX_TOKENS,
                        temperature = 0.7
                    )
                    onProgress("Connecting to Hermes...")
                    val response = com.darrenai.jarvis.network.HttpClient.post(
                        url,
                        com.darrenai.jarvis.network.ChatCompletionResponse::class.java,
                        body
                    )
                    when {
                        response != null && !response.choices.isNullOrEmpty() -> {
                            val answer = response.choices[0].message?.content ?: "No response"
                            onProgress("Done")
                            answer
                        }
                        else -> {
                            onProgress("No answer")
                            "I didn't get a response. Try again."
                        }
                    }
                } catch (e: Exception) {
                    onProgress("Error: ${e.message}")
                    "Connection failed. Please check your network."
                }
            }
            Mode.OFFLINE, Mode.UNKNOWN -> {
                onProgress("Thinking offline...")
                when {
                    messages.last().content.contains("hello", ignoreCase = true) -> "Hello! I'm JARVIS. I'm running in offline mode. Connect to Hermes for full AI capabilities."
                    messages.last().content.contains("what", ignoreCase = true) && messages.last().content.contains("name", ignoreCase = true) -> "I'm JARVIS, your personal AI assistant. Currently in offline mode."
                    messages.last().content.contains("time", ignoreCase = true) -> {
                        val now = java.text.SimpleDateFormat("HH:mm", java.util.Locale.US).format(
                            java.util.Date()
                        )
                        "It's $now. I'm in offline mode — connect to Hermes for more."
                    }
                    else -> "I'm running in offline mode right now. Connect to Hermes OmniRoute for full AI capabilities. Try asking: 'What can you do?'"
                }
            }
        }
    }
}
