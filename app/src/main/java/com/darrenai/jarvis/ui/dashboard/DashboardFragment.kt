package com.darrenai.jarvis.ui.dashboard

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.darrenai.jarvis.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animate arc reactor pulse
        val arcReactor = view.findViewById<View>(R.id.arc_reactor)
        val pulseAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.arc_reactor_pulse)
        arcReactor.startAnimation(pulseAnim)

        // Load system metrics
        loadSystemMetrics(view)

        // Quick action buttons
        view.findViewById<View>(R.id.btn_morning_briefing).setOnClickListener {
            // Trigger morning briefing via AI service
            triggerAction("Give me the morning briefing")
        }

        view.findViewById<View>(R.id.btn_health_check).setOnClickListener {
            triggerAction("Run a health check on all systems")
        }

        view.findViewById<View>(R.id.btn_status_report).setOnClickListener {
            triggerAction("Generate a status report")
        }

        view.findViewById<View>(R.id.btn_gateway_watch).setOnClickListener {
            triggerAction("Check gateway status")
        }

        // Load agent workload
        loadAgentWorkload(view)
    }

    private fun loadSystemMetrics(view: View) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            // Simulate fetching system metrics from Hermes endpoint
            // In production, this would hit http://<pc-ip>:20128/v1/ or a status endpoint
            try {
                val cpu = (10..45).random()
                val ram = (30..65).random()
                val battery = (70..100).random()
                val uptimeHours = (1..72).random()

                withContext(Dispatchers.Main) {
                    view.findViewById<TextView>(R.id.txt_cpu_value).text = "${cpu}%"
                    view.findViewById<ProgressBar>(R.id.progress_cpu).progress = cpu

                    view.findViewById<TextView>(R.id.txt_ram_value).text = "${ram}%"
                    view.findViewById<ProgressBar>(R.id.progress_ram).progress = ram

                    view.findViewById<TextView>(R.id.txt_battery_value).text = "${battery}%"
                    view.findViewById<ProgressBar>(R.id.progress_battery).progress = battery

                    view.findViewById<TextView>(R.id.txt_uptime).text = "Uptime: ${uptimeHours}h"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.findViewById<TextView>(R.id.txt_system_status).text = "OFFLINE"
                }
            }
        }
    }

    private fun loadAgentWorkload(view: View) {
        val agents = listOf(
            AgentInfo("JARVIS", "Active", R.color.agent_jarvis),
            AgentInfo("Dev Agent", "Idle", R.color.agent_dev),
            AgentInfo("Research", "Idle", R.color.agent_research),
            AgentInfo("Assistant", "Active", R.color.agent_assistant)
        )

        val container = view.findViewById<android.widget.LinearLayout>(R.id.layout_agents)
        container.removeAllViews()

        for (agent in agents) {
            val row = layoutInflater.inflate(R.layout.item_agent_row, container, false)
            row.findViewById<TextView>(R.id.txt_agent_name).text = agent.name
            row.findViewById<TextView>(R.id.txt_agent_status).text = agent.status
            val dot = row.findViewById<View>(R.id.agent_status_dot)
            dot.backgroundTintList = android.content.res.ColorStateList.valueOf(
                resources.getColor(agent.colorRes, null)
            )
            container.addView(row)
        }
    }

    private fun triggerAction(prompt: String) {
        // Navigate to chat tab via BottomNavigationView
        (activity as? androidx.appcompat.app.AppCompatActivity)?.let { app ->
            val bottomNav = app.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
            bottomNav.selectedItemId = R.id.nav_chat
        }
    }

}
