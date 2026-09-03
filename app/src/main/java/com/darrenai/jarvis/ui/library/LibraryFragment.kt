package com.darrenai.jarvis.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darrenai.jarvis.R

class LibraryFragment : Fragment() {

    private lateinit var adapter: LibraryAdapter
    private val docs = mutableListOf<LibraryDoc>()

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

        // Load mock docs for now
        loadDocuments()
    }

    private fun loadDocuments() {
        // In production, this would fetch from Hermes session storage or a docs endpoint
        docs.clear()
        docs.addAll(listOf(
            LibraryDoc("Morning Briefing — Sep 4", "JARVIS", "2026-09-04 07:00"),
            LibraryDoc("System Health Report", "JARVIS", "2026-09-04 06:00"),
            LibraryDoc("Competitor News Digest", "BlogWatcher", "2026-09-03 09:00"),
            LibraryDoc("Weekly Review Plan", "JARVIS", "2026-09-02 18:00"),
            LibraryDoc("Battery Saver Config", "JARVIS", "2026-09-01 14:00")
        ))
        adapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun updateEmptyState() {
        view?.findViewById<View>(R.id.empty_library)?.visibility =
            if (docs.isEmpty()) View.VISIBLE else View.GONE
    }
}
