package com.darrenai.jarvis.model

/**
 * Single chat message used across the app.
 * Placed in its own file so both MainActivity and ChatAdapter can import it
 * without the circular dependency that would arise from keeping it inside
 * ConnectivityManager.
 */
data class ChatMessage(
    val role: Role,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    enum class Role {
        USER,
        ASSISTANT,
        SYSTEM
    }
}
