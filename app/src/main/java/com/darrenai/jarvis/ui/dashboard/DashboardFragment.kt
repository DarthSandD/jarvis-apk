package com.darrenai.jarvis.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.darrenai.jarvis.R
import com.darrenai.jarvis.ai.AiProvider
import com.darrenai.jarvis.ai.AiService
import com.darrenai.jarvis.ai.HermesProvider
import com.darrenai.jarvis.ui.face.ArcReactorView
import com.darrenai.jarvis.ui.face.FaceActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

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

        // Greeting by time of day
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 5..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            else -> "Good evening"
        }
        view.findViewById<TextView>(R.id.txt_home_greeting)?.text = "$greeting, Boss"

        // Hero orb idles gently
        view.findViewById<ArcReactorView>(R.id.home_orb)?.apply {
            setState(ArcReactorView.State.IDLE)
            setLevel(0.15f)
        }

        // Endpoint + latency check
        val aiService = AiService.getInstance(requireContext())
        val latencyView = view.findViewById<TextView>(R.id.txt_home_latency)
        val dotView = view.findViewById<TextView>(R.id.txt_home_status_dot)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val t0 = System.currentTimeMillis()
            val err = try {
                aiService.healthCheck(AiProvider.Hermes)
            } catch (e: Exception) {
                com.darrenai.jarvis.ai.AiError.NetworkError(e.message ?: "failed")
            }
            val ms = System.currentTimeMillis() - t0
            withContext(Dispatchers.Main) {
                if (!isAdded) return@withContext
                if (err == null) {
                    latencyView?.text = "Connected · ${ms}ms · darren-1212"
                    dotView?.text = "● ONLINE"
                    dotView?.setTextColor(resources.getColor(R.color.jarvis_online, null))
                } else {
                    latencyView?.text = "Unreachable — check Wi-Fi / Settings"
                    dotView?.text = "● OFFLINE"
                    dotView?.setTextColor(resources.getColor(R.color.jarvis_gold, null))
                }
            }
        }

        // Quick actions
        view.findViewById<View>(R.id.btn_home_talk)?.setOnClickListener {
            selectTab(R.id.nav_voice)
        }
        view.findViewById<View>(R.id.btn_home_chat)?.setOnClickListener {
            selectTab(R.id.nav_chat)
        }
        view.findViewById<View>(R.id.btn_home_face)?.setOnClickListener {
            FaceActivity.show(requireContext())
        }
        view.findViewById<View>(R.id.btn_home_briefing)?.setOnClickListener {
            selectTab(R.id.nav_chat)
        }
    }

    private fun selectTab(itemId: Int) {
        (activity as? androidx.appcompat.app.AppCompatActivity)?.let { app ->
            app.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
                ?.selectedItemId = itemId
        }
    }
}
