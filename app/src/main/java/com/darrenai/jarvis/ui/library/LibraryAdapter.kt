package com.darrenai.jarvis.ui.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.darrenai.jarvis.R

class LibraryAdapter(private val docs: List<LibraryDoc>) :
    RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {

    class LibraryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txt_doc_title)
        val agent: TextView = view.findViewById(R.id.txt_doc_agent)
        val date: TextView = view.findViewById(R.id.txt_doc_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_library_doc, parent, false)
        return LibraryViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val doc = docs[position]
        holder.title.text = doc.title
        holder.agent.text = doc.agent
        holder.date.text = doc.date
    }

    override fun getItemCount() = docs.size
}
