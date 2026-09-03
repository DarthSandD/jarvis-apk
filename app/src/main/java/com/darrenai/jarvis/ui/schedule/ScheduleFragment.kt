package com.darrenai.jarvis.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darrenai.jarvis.R
import com.darrenai.jarvis.ai.HermesProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ScheduleFragment : Fragment() {

    private lateinit var adapter: CronJobAdapter
    private val cronJobs = mutableListOf<CronJob>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CronJobAdapter(cronJobs, object : CronJobAdapter.CronActionListener {
            override fun onRun(job: CronJob) {
                runCronJob(job)
            }

            override fun onTogglePause(job: CronJob) {
                toggleCronJob(job)
            }
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_schedule)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Load cron jobs
        loadCronJobs()

        // Refresh button
        view.findViewById<View>(R.id.btn_refresh_schedule).setOnClickListener {
            loadCronJobs()
        }
    }

    private fun loadCronJobs() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val provider = HermesProvider(requireContext())
                val response = provider.fetchCronJobs()
                val jobs = parseCronJobs(response)

                withContext(Dispatchers.Main) {
                    cronJobs.clear()
                    cronJobs.addAll(jobs)
                    adapter.notifyDataSetChanged()
                    updateEmptyState()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Show mock data if endpoint not available
                    cronJobs.clear()
                    cronJobs.addAll(getMockCronJobs())
                    adapter.notifyDataSetChanged()
                    updateEmptyState()
                }
            }
        }
    }

    private fun parseCronJobs(response: String): List<CronJob> {
        val jobs = mutableListOf<CronJob>()
        try {
            val json = JSONObject(response)
            val jobsArray = json.optJSONArray("jobs") ?: return jobs
            for (i in 0 until jobsArray.length()) {
                val job = jobsArray.getJSONObject(i)
                jobs.add(CronJob(
                    id = job.optString("id"),
                    name = job.optString("name"),
                    schedule = job.optString("schedule"),
                    status = job.optString("status", "active"),
                    lastRun = job.optString("last_run", "Unknown")
                ))
            }
        } catch (e: Exception) {
            // Fallback to mock
            return getMockCronJobs()
        }
        return jobs
    }

    private fun runCronJob(job: CronJob) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val provider = HermesProvider(requireContext())
                provider.triggerCronJob(job.id)
            } catch (e: Exception) {
                // Silent fail — will show mock
            }
        }
    }

    private fun toggleCronJob(job: CronJob) {
        job.status = if (job.status == "active") "paused" else "active"
        adapter.notifyDataSetChanged()
    }

    private fun updateEmptyState() {
        val emptyView = view?.findViewById<View>(R.id.empty_schedule)
        emptyView?.visibility = if (cronJobs.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun getMockCronJobs(): List<CronJob> {
        return listOf(
            CronJob("1", "Jarvis Morning Briefing", "07:00 WIB Daily", "active", "Today 07:00"),
            CronJob("2", "System Health Check", "06:00 WIB Daily", "active", "Today 06:00"),
            CronJob("3", "Battery Saver Monitor", "Every 2h", "active", "2h ago"),
            CronJob("4", "BlogWatcher — Competitor News", "Weekdays 9am", "paused", "Yesterday 09:00"),
            CronJob("5", "Product Price Monitor", "Every 30m", "active", "30m ago")
        )
    }
}
