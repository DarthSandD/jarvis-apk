package com.darrenai.jarvis.ui.voice

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.darrenai.jarvis.R
import com.darrenai.jarvis.ai.AiService
import com.darrenai.jarvis.services.JarvisVoiceService
import com.darrenai.jarvis.ui.face.ArcReactorView
import com.darrenai.jarvis.ui.face.FaceActivity
import com.darrenai.jarvis.ui.settings.SettingsActivity
import kotlinx.coroutines.launch

class VoiceFragment : Fragment(), JarvisVoiceService.VoiceCallback {

    private lateinit var voiceService: JarvisVoiceService
    private lateinit var aiService: AiService
    private var isListening = false

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startListening()
        } else {
            view?.findViewById<TextView>(R.id.txt_voice_status)?.text =
                "Microphone permission denied"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_voice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        aiService = AiService.getInstance(requireContext())
        voiceService = JarvisVoiceService.getInstance(requireContext())

        orb()?.setState(ArcReactorView.State.IDLE)
        orb()?.setLevel(0.15f)

        view.findViewById<View>(R.id.fab_voice).setOnClickListener {
            if (isListening) stopListening() else checkPermissionAndStart()
        }

        view.findViewById<View>(R.id.btn_voice_history)?.setOnClickListener {
            selectTab(R.id.nav_library)
        }
        view.findViewById<View>(R.id.btn_voice_settings)?.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }
    }

    private fun orb(): ArcReactorView? = view?.findViewById(R.id.voice_orb)

    private fun selectTab(itemId: Int) {
        (activity as? androidx.appcompat.app.AppCompatActivity)?.let { app ->
            app.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)
                ?.selectedItemId = itemId
        }
    }

    private fun checkPermissionAndStart() {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.RECORD_AUDIO
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startListening()
        } else {
            requestPermission.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun startListening() {
        if (!voiceService.isRecognitionAvailable()) {
            view?.findViewById<TextView>(R.id.txt_voice_status)?.text =
                "Speech recognition not available"
            return
        }
        isListening = true
        FaceActivity.setState("listening", 0.3f, 0.2f)
        orb()?.setState(ArcReactorView.State.LISTENING)
        orb()?.setLevel(0.5f)
        view?.findViewById<TextView>(R.id.txt_voice_status)?.text = getString(R.string.voice_listening)
        voiceService.startListening(this)
    }

    private fun stopListening() {
        isListening = false
        view?.findViewById<TextView>(R.id.txt_voice_status)?.text = getString(R.string.voice_processing)
        orb()?.setState(ArcReactorView.State.THINKING)
        orb()?.setLevel(0.4f)
        voiceService.stopListening()
    }

    // ---- VoiceCallback implementation ----

    override fun onVoiceResult(text: String) {
        isListening = false
        if (!isAdded) return
        FaceActivity.setState("thinking", 0.6f, 0.4f)
        orb()?.setState(ArcReactorView.State.THINKING)
        orb()?.setLevel(0.5f)
        view?.findViewById<TextView>(R.id.txt_user_transcript)?.apply {
            this.text = text
            visibility = View.VISIBLE
        }

        view?.findViewById<TextView>(R.id.txt_voice_status)?.text = getString(R.string.voice_processing)

        val history = listOf(
            com.darrenai.jarvis.model.ChatMessage(
                com.darrenai.jarvis.model.ChatMessage.Role.USER,
                text
            )
        )

        viewLifecycleOwner.lifecycleScope.launch {
            val responseView = view?.findViewById<TextView>(R.id.txt_jarvis_response)
            val statusView = view?.findViewById<TextView>(R.id.txt_voice_status)
            val fullResponse = StringBuilder()

            aiService.chat(history, null) { event ->
                if (!isAdded) return@chat
                when (event) {
                    is com.darrenai.jarvis.ai.StreamEvent.Delta -> {
                        fullResponse.append(event.text)
                        activity?.runOnUiThread {
                            responseView?.apply {
                                this.text = fullResponse.toString()
                                visibility = View.VISIBLE
                            }
                        }
                    }
                    is com.darrenai.jarvis.ai.StreamEvent.Done -> {
                        activity?.runOnUiThread {
                            statusView?.text = getString(R.string.voice_tap_to_speak)
                            FaceActivity.setState("speaking", 0.8f, 0.6f)
                            orb()?.setState(ArcReactorView.State.SPEAKING)
                            orb()?.setLevel(0.8f)
                            voiceService.speak(fullResponse.toString()) {
                                activity?.runOnUiThread {
                                    FaceActivity.setState("idle", 0f, 0f)
                                    orb()?.setState(ArcReactorView.State.IDLE)
                                    orb()?.setLevel(0.15f)
                                }
                            }
                        }
                    }
                    is com.darrenai.jarvis.ai.StreamEvent.Error -> {
                        activity?.runOnUiThread {
                            FaceActivity.setState("idle", 0f, 0f)
                            orb()?.setState(ArcReactorView.State.IDLE)
                            orb()?.setLevel(0.15f)
                            responseView?.apply {
                                this.text = "⚠️ ${event.error.message}"
                                visibility = View.VISIBLE
                            }
                            statusView?.text = getString(R.string.voice_tap_to_speak)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onVoiceError(error: String) {
        isListening = false
        if (!isAdded) return
        FaceActivity.setState("idle", 0f, 0f)
        orb()?.setState(ArcReactorView.State.IDLE)
        orb()?.setLevel(0.15f)
        view?.findViewById<TextView>(R.id.txt_voice_status)?.text = error
    }

    override fun onDestroyView() {
        super.onDestroyView()
        voiceService.stopListening()
        // Do NOT call shutdown() here — it destroys the singleton
        // and the service is reused across tab switches
    }
}
