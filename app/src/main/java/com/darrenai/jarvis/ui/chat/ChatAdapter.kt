package com.darrenai.jarvis.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.darrenai.jarvis.R

class ChatAdapter(private val messages: List<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layoutUser: View = view.findViewById(R.id.layout_user_message)
        val layoutJarvis: View = view.findViewById(R.id.layout_jarvis_message)
        val txtUser: TextView = view.findViewById(R.id.txt_user_message)
        val txtJarvis: TextView = view.findViewById(R.id.txt_jarvis_message)
        val txtAgentName: TextView = view.findViewById(R.id.txt_agent_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val msg = messages[position]
        if (msg.isUser) {
            holder.layoutUser.visibility = View.VISIBLE
            holder.layoutJarvis.visibility = View.GONE
            holder.txtUser.text = msg.text
        } else {
            holder.layoutUser.visibility = View.GONE
            holder.layoutJarvis.visibility = View.VISIBLE
            holder.txtJarvis.text = msg.text
            holder.txtAgentName.text = "JARVIS"
        }
    }

    override fun getItemCount() = messages.size
}
