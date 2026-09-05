package com.darrenai.jarvis.ui.face

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.darrenai.jarvis.R

/**
 * Full-screen Jarvis Face visualizer.
 * Loads the Circuit Board (ai-visualizer) from assets and bridges
 * voice/chat state from Kotlin via JavaScript interface.
 *
 * State values: "idle" | "listening" | "thinking" | "speaking"
 *
 * Usage from any context:
 *   FaceActivity.show(context)
 *   FaceActivity.setState("speaking", 0.7f, 0.5f)
 */
class FaceActivity : Activity() {

    private lateinit var webView: WebView
    private var paused = false

    companion object {
        private var instance: FaceActivity? = null

        /** Launch the face over the current activity. */
        fun show(ctx: Context) {
            val intent = android.content.Intent(ctx, FaceActivity::class.java).apply {
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                        android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            ctx.startActivity(intent)
        }

        /** Push state from any thread. Safe to call when no face is showing. */
        fun setState(state: String, level: Float = 0f, env: Float = 0f) {
            instance?.setStateInternal(state, level, env)
        }

        /** Set the display name shown on the chip. */
        fun setName(name: String) {
            instance?.webView?.evaluateJavascript(
                "window.__faceState.name = '$name'; " +
                "if (window.AV && window.AV.name !== undefined) { window.AV.name = '$name'; }", null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // Immersive: hide status bar and navigation bar
        window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
                android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        webView = WebView(this).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = false
                displayZoomControls = false
                setSupportZoom(false)
                cacheMode = WebSettings.LOAD_NO_CACHE
                mediaPlaybackRequiresUserGesture = false
            }
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
            addJavascriptInterface(FaceBridge(), "JarvisFace")
            loadUrl("file:///android_asset/jarvis-face/index.html")
        }

        setContentView(webView)
        instance = this
    }

    override fun onResume() {
        super.onResume()
        paused = false
        webView.onResume()
        webView.evaluateJavascript(
            "if (window.audio && window.audio.context && window.audio.context.state === 'suspended') {" +
            "window.audio.context.resume(); }", null)
    }

    override fun onPause() {
        super.onPause()
        paused = true
        webView.onPause()
    }

    override fun onDestroy() {
        instance = null
        webView.destroy()
        super.onDestroy()
    }

    private fun setStateInternal(state: String, level: Float, env: Float) {
        if (paused) return
        runOnUiThread {
            webView.evaluateJavascript(
                "window.__setFaceState('$state', $level, $env);", null)
        }
    }

    inner class FaceBridge {
        @JavascriptInterface
        fun setState(state: String, level: Double, env: Double): String {
            webView.evaluateJavascript("window.__faceState = { state: '$state', level: $level, env: $env }", null)
            return "ok"
        }

        @JavascriptInterface
        fun now(): String {
            var result = "{}"
            webView.evaluateJavascript("JSON.stringify(window.__faceState || {})") { jsResult ->
                result = jsResult ?: "{}"
            }
            return result
        }
    }
}
