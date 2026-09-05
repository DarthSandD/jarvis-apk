package com.darrenai.jarvis.ui.voice

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.darrenai.jarvis.R
import com.darrenai.jarvis.ai.AiService
import com.darrenai.jarvis.services.JarvisVoiceService
import com.darrenai.jarvis.ui.face.FaceActivity
import kotlinx.coroutines.launch

class VoiceFragment : Fragment(), JarvisVoiceService.VoiceCallback {

    private lateinit var voiceService: JarvisVoiceService
    private lateinit var aiService: AiService
    private var isListening = false
    private val waveAnims = mutableListOf<Animation>()

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

        // Voice button
        view.findViewById<View>(R.id.fab_voice).setOnClickListener {
            if (isListening) {
                stopListening()
            } else {
                checkPermissionAndStart()
            }
        }

        // Animate outer ring (guarded — a missing anim must never crash the tab)
        runCatching {
            val ringOuter = view.findViewById<View>(R.id.voice_ring_outer)
            val pulseAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.arc_reactor_pulse)
            ringOuter.startAnimation(pulseAnim)
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
        view?.findViewById<TextView>(R.id.txt_voice_status)?.text = getString(R.string.voice_listening)
        view?.findViewById<LinearLayout>(R.id.layout_voice_waveform)?.visibility = View.VISIBLE

        // Animate waveform bars
        runCatching {
            val waveAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.voice_wave_anim)
            waveAnims.clear()
            for (i in 1..12) {
                val barId = resources.getIdentifier("wave_bar_$i", "id", requireContext().packageName)
                view?.findViewById<View>(barId)?.let { bar ->
                    bar.startAnimation(waveAnim)
                    waveAnims.add(waveAnim)
                }
            }
        }

        voiceService.startListening(this)
    }

    private fun stopListening() {
        isListening = false
        clearWaveAnims()
        view?.findViewById<TextView>(R.id.txt_voice_status)?.text = getString(R.string.voice_processing)
        view?.findViewById<LinearLayout>(R.id.layout_voice_waveform)?.visibility = View.GONE
        voiceService.stopListening()
    }

    private fun clearWaveAnims() {
        view?.let { v ->
            for (i in 1..12) {
                val barId = runCatching {
                    resources.getIdentifier("wave_bar_$i", "id", requireContext().packageName)
                }.getOrDefault(0)
                if (barId != 0) v.findViewById<View>(barId)?.clearAnimation()
            }
        }
        waveAnims.clear()
    }

    // ---- VoiceCallback implementation ----

    override fun onVoiceResult(text: String) {
        isListening = false
        if (!isAdded) return
        clearWaveAnims()
        FaceActivity.setState("thinking", 0.6f, 0.4f)
        // Show user transcript
        view?.findViewById<TextView>(R.id.txt_user_transcript)?.apply {
            this.text = text
            visibility = View.VISIBLE
        }

        view?.findViewById<TextView>(R.id.txt_voice_status)?.text = getString(R.string.voice_processing)

        // Send to AI via streaming chat
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
                            voiceService.speak(fullResponse.toString()) {
                                activity?.runOnUiThread {
                                    FaceActivity.setState("idle", 0f, 0f)
                                }
                            }
                        }
                    }
                    is com.darrenai.jarvis.ai.StreamEvent.Error -> {
                        activity?.runOnUiThread {
                            FaceActivity.setState("idle", 0f, 0f)
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
        clearWaveAnims()
        FaceActivity.setState("idle", 0f, 0f)
        view?.findViewById<TextView>(R.id.txt_voice_status)?.text = error
        view?.findViewById<LinearLayout>(R.id.layout_voice_waveform)?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearWaveAnims()
        voiceService.stopListening()
        // Do NOT call shutdown() here — it destroys the singleton
        // and the service is reused across tab switches
    }
}
