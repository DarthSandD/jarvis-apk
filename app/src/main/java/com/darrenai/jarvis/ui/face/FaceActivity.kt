package com.darrenai.jarvis.ui.face

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout

/**
 * Full-screen Jarvis Face visualizer — native Arc Reactor.
 * No server needed, no WebView. Runs entirely on-device.
 */
class FaceActivity : Activity() {

    private lateinit var arcReactor: ArcReactorView

    companion object {
        private var instance: FaceActivity? = null

        fun show(ctx: Context) {
            val intent = android.content.Intent(ctx, FaceActivity::class.java).apply {
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                        android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            ctx.startActivity(intent)
        }

        fun setState(state: String, level: Float = 0f, env: Float = 0f) {
            instance?.arcReactor?.let {
                val s = when (state.lowercase()) {
                    "listening" -> ArcReactorView.State.LISTENING
                    "thinking" -> ArcReactorView.State.THINKING
                    "speaking" -> ArcReactorView.State.SPEAKING
                    else -> ArcReactorView.State.IDLE
                }
                it.setState(s)
                it.setLevel(level)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
                android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        arcReactor = ArcReactorView(this)
        setContentView(arcReactor, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ))

        instance = this
    }

    override fun onDestroy() {
        instance = null
        super.onDestroy()
    }
}