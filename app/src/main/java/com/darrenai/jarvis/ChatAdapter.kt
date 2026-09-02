package com.darrenai.jarvis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.darrenai.jarvis.databinding.ItemChatMessageBinding
import com.darrenai.jarvis.model.ChatMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Modern chat adapter — renders user, assistant, and system messages
 * with asymmetric bubbles, timestamps, and avatar indicators.
 */
class ChatAdapter : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private val messages = mutableListOf<ChatMessage>()
    private var onNewMessageListener: (() -> Unit)? = null
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun setOnNewMessageListener(listener: () -> Unit) {
        onNewMessageListener = listener
    }

    fun addMessage(msg: ChatMessage) {
        messages.add(msg)
        notifyItemInserted(messages.size - 1)
        onNewMessageListener?.invoke()
    }

    fun updateMessage(index: Int, text: String) {
        if (index in messages.indices) {
            messages[index] = messages[index].copy(content = text)
            notifyItemChanged(index)
        }
    }

    fun removeMessageAt(index: Int) {
        if (index in messages.indices) {
            messages.removeAt(index)
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, messages.size - index)
        }
    }

    fun clear() {
        messages.clear()
        notifyDataSetChanged()
    }

    fun getMessage(index: Int): ChatMessage? =
        if (index in messages.indices) messages[index] else null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemChatMessageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position], position)
    }

    override fun getItemCount(): Int = messages.size

    inner class MessageViewHolder(
        private val binding: ItemChatMessageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(msg: ChatMessage, position: Int) {
            val time = timeFormat.format(Date())

            // Reset visibility
            binding.containerUser.visibility = View.GONE
            binding.containerAssistant.visibility = View.GONE
            binding.txtSystemMessage.visibility = View.GONE

            when (msg.role) {
                ChatMessage.Role.USER -> {
                    binding.containerUser.visibility = View.VISIBLE
                    binding.txtUserMessage.text = msg.content
                    binding.txtUserTime.text = time
                }
                ChatMessage.Role.ASSISTANT -> {
                    binding.containerAssistant.visibility = View.VISIBLE
                    binding.txtAssistantMessage.text = msg.content
                    binding.txtAssistantTime.text = time

                    // Hide avatar for consecutive assistant messages
                    val showAvatar = position == 0 ||
                        messages.getOrNull(position - 1)?.role != ChatMessage.Role.ASSISTANT
                    binding.avatarAssistant.visibility = if (showAvatar) View.VISIBLE else View.INVISIBLE
                }
                ChatMessage.Role.SYSTEM -> {
                    binding.txtSystemMessage.visibility = View.VISIBLE
                    binding.txtSystemMessage.text = msg.content
                }
            }
        }
    }
}
