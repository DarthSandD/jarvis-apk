package com.darrenai.jarvis

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager as NetConnManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.darrenai.jarvis.model.ChatMessage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Optimized connectivity manager for JARVIS.
 *
 * Features:
 * - Fast WiFi-first network detection (lower latency reconnect)
 * - Automatic retry with exponential backoff on connection failure
 * - Background keepalive ping to maintain connection awareness
 * - Optimistic reconnection after network events (immediate retry, no waiting)
 * - Tunable via SharedPreferences: reconnect_attempts, reconnect_base_delay_ms, keepalive_interval_ms
 */
class ConnectivityManager(private val context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("jarvis_settings", Context.MODE_PRIVATE)
    }

    init {
        ConnectivityManager.Companion._prefs = prefs
    }

    companion object {
        @Volatile private var _prefs: SharedPreferences? = null

        var serverIp: String
            get() = ConnectivityManager.Companion._prefs?.getString("server_ip", "10.212.104.124") ?: "10.212.104.124"
            set(value) { val p = ConnectivityManager.Companion._prefs; if (p != null) { p.edit().putString("server_ip", value); } }

        var serverPort: Int
            get() = ConnectivityManager.Companion._prefs?.getInt("server_port", 20128) ?: 20128
            set(value) { val p = ConnectivityManager.Companion._prefs; if (p != null) { p.edit().putInt("server_port", value); } }

        // Tunable retry settings
        var reconnectAttempts: Int
            get() = ConnectivityManager.Companion._prefs?.getInt("reconnect_attempts", 3) ?: 3
            set(value) { val p = ConnectivityManager.Companion._prefs; if (p != null) { p.edit().putInt("reconnect_attempts", value); } }

        var reconnectBaseDelayMs: Int
            get() = ConnectivityManager.Companion._prefs?.getInt("reconnect_base_delay_ms", 500) ?: 500
            set(value) { val p = ConnectivityManager.Companion._prefs; if (p != null) { p.edit().putInt("reconnect_base_delay_ms", value); } }

        var keepaliveIntervalMs: Long
            get() = ConnectivityManager.Companion._prefs?.getLong("keepalive_interval_ms", 30000L) ?: 30000L
            set(value) { val p = ConnectivityManager.Companion._prefs; if (p != null) { p.edit().putLong("keepalive_interval_ms", value); } }

        var enableKeepalive: Boolean
            get() = ConnectivityManager.Companion._prefs?.getBoolean("enable_keepalive", true) ?: true
            set(value) { val p = ConnectivityManager.Companion._prefs; if (p != null) { p.edit().putBoolean("enable_keepalive", value); } }

        const val MAX_CONTEXT = 4096
        const val MAX_TOKENS = 512
        private const val CONNECT_TIMEOUT_MS = 10_000
        private const val READ_TIMEOUT_MS = 25_000
    }

    enum class Mode { ONLINE, OFFLINE, UNKNOWN }

    // Flow for reactive connection state updates
    private val _connectionState = MutableSharedFlow<Pair<Mode, String?>>(replay = 1)
    val connectionState = _connectionState.asSharedFlow()

    var listener: ((Mode) -> Unit)? = null

    // Track current connection state
    @Volatile private var currentMode: Mode = Mode.UNKNOWN
    @Volatile private var isMonitoring = false
    private var keepaliveJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val networkCallback = object : NetConnManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            // Network just came back — trigger immediate optimistic reconnection
            currentMode = Mode.ONLINE
            listener?.invoke(Mode.ONLINE)
            _connectionState.tryEmit(Mode.ONLINE to null)
            // Fire a quick health check immediately (don't wait for keepalive cycle)
            scope.launch { quickHealthCheck() }
        }

        override fun onLost(network: Network) {
            currentMode = Mode.OFFLINE
            listener?.invoke(Mode.OFFLINE)
            _connectionState.tryEmit(Mode.OFFLINE to null)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            val hasInternet = networkCapabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )
            val hasWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

            // Prefer WiFi — it's faster and more stable for LAN connections
            if (hasInternet) {
                currentMode = Mode.ONLINE
                listener?.invoke(Mode.ONLINE)
                _connectionState.tryEmit(Mode.ONLINE to if (hasWifi) "wifi" else "other")
                // Quick reconnect attempt on carrier/network change
                scope.launch { quickHealthCheck() }
            } else {
                if (currentMode != Mode.OFFLINE) {
                    currentMode = Mode.OFFLINE
                    listener?.invoke(Mode.OFFLINE)
                    _connectionState.tryEmit(Mode.OFFLINE to null)
                }
            }
        }
    }

    private val connectivityManager: NetConnManager by lazy {
        context.getSystemService(NetConnManager::class.java)
    }

    /**
     * Start monitoring network connectivity with WiFi-first preference.
     * Registered callbacks fire faster on WiFi networks.
     */
    fun startMonitoring() {
        if (isMonitoring) return
        isMonitoring = true

        // Build request with WiFi transport preferred — faster detection on WiFi networks
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)  // WiFi-first for lower latency
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR) // Fallback to cellular
            .setNetworkSpecifier("JARVIS")
            .build()

        try {
            connectivityManager.registerNetworkCallback(request, networkCallback)
        } catch (e: Exception) {
            // Fallback to basic request if app-specific fails
            val basicRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
            connectivityManager.registerNetworkCallback(basicRequest, networkCallback)
        }

        // Start keepalive pinger
        startKeepalive()

        // Initial connection state check
        scope.launch { quickHealthCheck() }
    }

    /**
     * Stop all monitoring and background tasks.
     */
    fun stopMonitoring() {
        isMonitoring = false
        connectivityManager.unregisterNetworkCallback(networkCallback)
        keepaliveJob?.cancel()
        keepaliveJob = null
        scope.cancel()
    }

    /**
     * Get current connection mode by checking actual network.
     * Uses cached mode if available (avoids repeated network calls).
     */
    fun getCurrentMode(): Mode {
        if (currentMode != Mode.UNKNOWN) {
            return currentMode
        }
        val cm = context.getSystemService(NetConnManager::class.java)
        val activeNetwork = cm?.activeNetwork ?: return Mode.OFFLINE
        val caps = cm.getNetworkCapabilities(activeNetwork) ?: return Mode.OFFLINE
        return when {
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> Mode.ONLINE
            else -> Mode.OFFLINE
        }
    }

    fun isOnline(): Boolean = currentMode == Mode.ONLINE

    /**
     * Send a message with automatic retry on connection failure.
     * Each retry uses exponential backoff.
     */
    suspend fun sendMessage(
        messages: List<ChatMessage>,
        mode: Mode,
        onProgress: (String) -> Unit = {}
    ): String {
        return when (mode) {
            Mode.ONLINE -> {
                // Attempt with retries
                retryWithBackoff(
                    attempts = reconnectAttempts,
                    baseDelayMs = reconnectBaseDelayMs,
                    onProgress = onProgress
                ) { attempt ->
                    if (attempt > 1) {
                        onProgress("Retrying connection (${attempt}/${reconnectAttempts})...")
                    }
                    sendToOneServer(messages, onProgress)
                }
            }
            Mode.OFFLINE, Mode.UNKNOWN -> {
                onProgress("Thinking offline...")
                generateOfflineResponse(messages)
            }
        }
    }

    /**
     * Send to server with a single attempt. Returns result or throws.
     */
    private suspend fun sendToOneServer(messages: List<ChatMessage>, onProgress: (String) -> Unit): String {
        val url = "http://${serverIp}:${serverPort}/v1/chat/completions"
        val body = com.darrenai.jarvis.network.ChatRequest(
            model = "auto/best-fast",
            messages = messages.map { com.darrenai.jarvis.network.ChatMessageDto(it.role.name.lowercase(), it.content) },
            maxTokens = MAX_TOKENS,
            temperature = 0.7
        )
        onProgress("Connecting...")
        val response = com.darrenai.jarvis.network.HttpClient.post(
            url,
            com.darrenai.jarvis.network.ChatCompletionResponse::class.java,
            body,
            connectTimeoutMs = CONNECT_TIMEOUT_MS,
            readTimeoutMs = READ_TIMEOUT_MS
        )
        return when {
            response != null && !response.choices.isNullOrEmpty() -> {
                val answer = response.choices[0].message?.content ?: "No response"
                onProgress("Done")
                answer
            }
            else -> {
                onProgress("No answer")
                throw Exception("Empty response from server")
            }
        }
    }

    /**
     * Retry a block of code with exponential backoff.
     * Returns the block result on success, throws the last error after all retries.
     */
    private suspend fun <T> retryWithBackoff(
        attempts: Int,
        baseDelayMs: Int,
        onProgress: (String) -> Unit = {},
        block: suspend (Int) -> T
    ): T {
        var lastError: Exception? = null
        for (attempt in 1..attempts) {
            try {
                return block(attempt)
            } catch (e: Exception) {
                lastError = e
                if (attempt < attempts) {
                    val delayMs = baseDelayMs * (1L shl (attempt - 1))  // Exponential backoff: 500, 1000, 2000...
                    onProgress("Waiting ${delayMs}ms before retry...")
                    delay(delayMs.toLong())
                }
            }
        }
        throw lastError ?: Exception("Unknown error after $attempts retries")
    }

    /**
     * Lightweight health check — just a quick ping to the server.
     * Used for reconnection verification and keepalive.
     */
    suspend fun quickHealthCheck(): Boolean {
        if (!enableKeepalive) return false
        val url = "http://${serverIp}:${serverPort}/v1/chat/completions"
        return try {
            val request = com.darrenai.jarvis.network.ChatRequest(
                model = "auto/best-fast",
                messages = listOf(com.darrenai.jarvis.network.ChatMessageDto("user", "ping")),
                maxTokens = 5,
                temperature = 0.7
            )
            val response: com.darrenai.jarvis.network.ChatCompletionResponse? = com.darrenai.jarvis.network.HttpClient.post(
                url,
                com.darrenai.jarvis.network.ChatCompletionResponse::class.java,
                request,
                connectTimeoutMs = 5000,
                readTimeoutMs = 10000
            )
            val ok = response != null && !response.choices.isNullOrEmpty()
            if (ok) {
                currentMode = Mode.ONLINE
            } else {
                currentMode = Mode.OFFLINE
            }
            ok
        } catch (e: Exception) {
            currentMode = Mode.OFFLINE
            false
        }
    }

    /**
     * Background keepalive — periodically checks if server is reachable.
     * Keeps the app aware of connection state changes even without user interaction.
     */
    private fun startKeepalive() {
        if (!enableKeepalive) return
        keepaliveJob = scope.launch {
            while (isActive) {
                delay(keepaliveIntervalMs)
                if (!isActive) break
                quickHealthCheck()
                // Emit state to listeners
                listener?.invoke(currentMode)
                _connectionState.tryEmit(currentMode to null)
            }
        }
    }

    /**
     * Generate offline response for common queries.
     */
    private fun generateOfflineResponse(messages: List<ChatMessage>): String {
        val last = messages.lastOrNull() ?: return "I'm offline right now."
        return when {
            last.content.contains("hello", ignoreCase = true) ||
            last.content.contains("hi", ignoreCase = true) ||
            last.content.contains("hey", ignoreCase = true) -> {
                "Hello! I'm JARVIS. I'm running in offline mode right now. Connect to Hermes for full AI capabilities."
            }
            last.content.contains("what", ignoreCase = true) && last.content.contains("name", ignoreCase = true) -> {
                "I'm JARVIS, your personal AI assistant. Currently in offline mode."
            }
            last.content.contains("time", ignoreCase = true) -> {
                val now = java.text.SimpleDateFormat("HH:mm", java.util.Locale.US).format(java.util.Date())
                "It's $now. I'm in offline mode — connect to Hermes for more."
            }
            last.content.contains("help", ignoreCase = true) ||
            last.content.contains("can you do", ignoreCase = true) -> {
                "I can help with voice commands, reminders, and AI chat. Connect to Hermes OmniRoute for full AI capabilities. Try: 'Connect to Hermes' when you're back online."
            }
            else -> {
                "I'm running in offline mode right now. Connect to Hermes OmniRoute for full AI capabilities."
            }
        }
    }
}
