package com.darrenai.jarvis.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darrenai.jarvis.JarvisApplication
import com.darrenai.jarvis.R
import com.darrenai.jarvis.database.ReminderEntity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LibraryFragment : Fragment() {

    private lateinit var adapter: LibraryAdapter
    private val docs = mutableListOf<LibraryDoc>()
    private val allDocs = mutableListOf<LibraryDoc>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LibraryAdapter(docs)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_library)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Search filters the memory list
        view.findViewById<EditText>(R.id.edit_memory_search)?.let { search ->
            search.addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
                override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {
                    filter(s?.toString().orEmpty())
                }
                override fun afterTextChanged(s: android.text.Editable?) {}
            })
        }

        // Add a memory note
        view.findViewById<View>(R.id.btn_memory_add)?.setOnClickListener {
            showAddDialog()
        }

        loadDocuments()
    }

    private fun filter(query: String) {
        docs.clear()
        if (query.isBlank()) {
            docs.addAll(allDocs)
        } else {
            docs.addAll(allDocs.filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.agent.contains(query, ignoreCase = true)
            })
        }
        runCatching { adapter.notifyDataSetChanged() }
        updateEmptyState()
    }

    private fun loadDocuments() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val loaded = try {
                val dao = (requireContext().applicationContext as JarvisApplication).reminderDao
                dao.getAll().first().map { e ->
                    LibraryDoc(
                        title = e.title.ifBlank { e.description.take(48) },
                        agent = if (e.completed) "Done" else "Memory",
                        date = fmt(e.createdAt)
                    )
                }
            } catch (e: Exception) {
                emptyList()
            }
            withContext(Dispatchers.Main) {
                if (!isAdded) return@withContext
                allDocs.clear()
                if (loaded.isNotEmpty()) {
                    allDocs.addAll(loaded)
                } else {
                    allDocs.addAll(seedDocs())
                }
                filter(view?.findViewById<EditText>(R.id.edit_memory_search)?.text?.toString().orEmpty())
            }
        }
    }

    private fun showAddDialog() {
        val input = TextInputEditText(requireContext()).apply {
            hint = "Remember this…"
            setPadding(32, 24, 32, 24)
        }
        AlertDialog.Builder(requireContext(), R.style.Theme_JARVIS)
            .setTitle("New memory")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val text = input.text?.toString()?.trim().orEmpty()
                if (text.isNotEmpty()) saveMemory(text)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveMemory(text: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val dao = (requireContext().applicationContext as JarvisApplication).reminderDao
                dao.insert(ReminderEntity(title = text.take(80), description = text))
            } catch (e: Exception) { }
            withContext(Dispatchers.Main) { loadDocuments() }
        }
    }

    private fun updateEmptyState() {
        view?.findViewById<View>(R.id.empty_library)?.visibility =
            if (docs.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun fmt(ts: Long): String {
        return try {
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(ts))
        } catch (e: Exception) {
            ""
        }
    }

    private fun seedDocs(): List<LibraryDoc> {
        return listOf(
            LibraryDoc("Morning Briefing — daily 07:00", "JARVIS", "schedule"),
            LibraryDoc("System Health Report", "JARVIS", "system"),
            LibraryDoc("Competitor News Digest", "BlogWatcher", "content")
        )
    }
}
