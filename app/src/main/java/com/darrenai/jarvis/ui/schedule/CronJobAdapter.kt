package com.darrenai.jarvis.ui.schedule

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.darrenai.jarvis.R

class CronJobAdapter(
    private val jobs: List<CronJob>,
    private val listener: CronActionListener
) : RecyclerView.Adapter<CronJobAdapter.CronViewHolder>() {

    interface CronActionListener {
        fun onRun(job: CronJob)
        fun onTogglePause(job: CronJob)
    }

    class CronViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txt_cron_name)
        val schedule: TextView = view.findViewById(R.id.txt_cron_schedule)
        val lastRun: TextView = view.findViewById(R.id.txt_cron_last_run)
        val statusChip: com.google.android.material.chip.Chip = view.findViewById(R.id.chip_cron_status)
        val statusDot: View = view.findViewById(R.id.cron_status_dot)
        val btnRun: com.google.android.material.button.MaterialButton = view.findViewById(R.id.btn_cron_run)
        val btnToggle: com.google.android.material.button.MaterialButton = view.findViewById(R.id.btn_cron_toggle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CronViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cron_job, parent, false)
        return CronViewHolder(view)
    }

    override fun onBindViewHolder(holder: CronViewHolder, position: Int) {
        val job = jobs[position]
        holder.name.text = job.name
        holder.schedule.text = "📅 ${job.schedule}"
        holder.lastRun.text = "Last: ${job.lastRun}"

        if (job.status == "active") {
            holder.statusChip.text = "ACTIVE"
            holder.statusChip.setTextColor(holder.itemView.context.getColor(R.color.jarvis_online))
            holder.statusDot.backgroundTintList = ColorStateList.valueOf(
                holder.itemView.context.getColor(R.color.jarvis_online)
            )
            holder.btnToggle.text = "Pause"
        } else {
            holder.statusChip.text = "PAUSED"
            holder.statusChip.setTextColor(holder.itemView.context.getColor(R.color.jarvis_gold))
            holder.statusDot.backgroundTintList = ColorStateList.valueOf(
                holder.itemView.context.getColor(R.color.jarvis_gold)
            )
            holder.btnToggle.text = "Resume"
        }

        holder.btnRun.setOnClickListener { listener.onRun(job) }
        holder.btnToggle.setOnClickListener { listener.onTogglePause(job) }
    }

    override fun getItemCount() = jobs.size
}
