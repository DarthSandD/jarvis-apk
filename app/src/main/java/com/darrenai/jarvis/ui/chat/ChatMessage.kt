package com.darrenai.jarvis.ui.chat

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun timeLabel(): String {
        return try {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
        } catch (e: Exception) {
            ""
        }
    }
}
