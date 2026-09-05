package com.darrenai.jarvis.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darrenai.jarvis.R
import com.darrenai.jarvis.ai.AiService
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private lateinit var aiService: AiService
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()
    private var selectedAgent: String = "jarvis"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            aiService = AiService.getInstance(requireContext())

            // Setup agent selector chips
            setupAgentChips(view)

            // Setup RecyclerView
            chatAdapter = ChatAdapter(messages)
            val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_chat)
            recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            recyclerView.adapter = chatAdapter

            // Empty state
            updateEmptyState(view)

            // Input bar
            val editMessage = view.findViewById<android.widget.EditText>(R.id.edit_message)
            val btnSend = view.findViewById<ImageButton>(R.id.btn_send)

            btnSend.setOnClickListener {
                val text = editMessage.text.toString().trim()
                if (text.isNotEmpty()) {
                    sendMessage(view, text)
                    editMessage.text.clear()
                }
            }

            // Voice button — navigate to voice tab
            view.findViewById<ImageButton>(R.id.btn_voice).setOnClickListener {
                (activity as? androidx.appcompat.app.AppCompatActivity)?.let { app ->
                    val bottomNav = app.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
                    bottomNav.selectedItemId = R.id.nav_voice
                }
            }
        } catch (e: Exception) {
            // Never crash the tab — surface the error in the empty state.
            runCatching {
                view.findViewById<View>(R.id.empty_state)?.visibility = View.VISIBLE
                view.findViewById<android.widget.TextView>(R.id.txt_chat_error)?.apply {
                    text = "Couldn't start chat: ${e.message}"
                    visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupAgentChips(view: View) {
        val chipGroup = view.findViewById<ChipGroup>(R.id.chip_group_agents)
        val agents = listOf("JARVIS" to "jarvis", "Dev" to "dev", "Research" to "research", "Assistant" to "assistant")

        for ((label, id) in agents) {
            val chip = Chip(requireContext())
            chip.text = label
            chip.isCheckable = true
            chip.isChecked = id == selectedAgent
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) selectedAgent = id
            }
            chipGroup.addView(chip)
        }
    }

    private fun sendMessage(view: View, text: String) {
        // Add user message
        messages.add(ChatMessage(text, true))
        runCatching { chatAdapter.notifyItemInserted(messages.size - 1) }
        updateEmptyState(view)
        scrollToBottom(view)

        // Get AI response via streaming chat
        val history = messages.map {
            com.darrenai.jarvis.model.ChatMessage(
                if (it.isUser) com.darrenai.jarvis.model.ChatMessage.Role.USER
                else com.darrenai.jarvis.model.ChatMessage.Role.ASSISTANT,
                it.text
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            aiService.chat(history, null) { event ->
                if (!isAdded) return@chat
                when (event) {
                    is com.darrenai.jarvis.ai.StreamEvent.Delta -> {
                        // Update or append the assistant message
                        val lastIdx = messages.size - 1
                        if (lastIdx >= 0 && !messages[lastIdx].isUser) {
                            messages[lastIdx] = ChatMessage(
                                messages[lastIdx].text + event.text, false
                            )
                            runCatching { chatAdapter.notifyItemChanged(lastIdx) }
                        } else {
                            messages.add(ChatMessage(event.text, false))
                            runCatching { chatAdapter.notifyItemInserted(messages.size - 1) }
                        }
                        scrollToBottom(view)
                    }
                    is com.darrenai.jarvis.ai.StreamEvent.Done -> {
                        // Final text already shown via Delta
                    }
                    is com.darrenai.jarvis.ai.StreamEvent.Error -> {
                        messages.add(ChatMessage("⚠️ ${event.error.message}", false))
                        runCatching { chatAdapter.notifyItemInserted(messages.size - 1) }
                        scrollToBottom(view)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun updateEmptyState(view: View) {
        runCatching {
            val emptyState = view.findViewById<View>(R.id.empty_state)
            emptyState.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun scrollToBottom(view: View) {
        if (messages.isEmpty()) return
        runCatching {
            val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_chat)
            recyclerView.scrollToPosition(messages.size - 1)
        }
    }
}
