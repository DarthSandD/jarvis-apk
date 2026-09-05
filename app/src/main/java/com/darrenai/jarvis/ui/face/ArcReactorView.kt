package com.darrenai.jarvis.ui.face

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.min
import kotlin.math.sin

/**
 * Native Arc Reactor face visualizer.
 * Animates rings based on voice state: idle, listening, thinking, speaking.
 * No server needed — runs entirely on-device.
 */
class ArcReactorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    enum class State { IDLE, LISTENING, THINKING, SPEAKING }

    private var state = State.IDLE
    private var level = 0f
    private var animPhase = 0f

    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }
    private val corePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val chipPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#00D4FF")
        textSize = 28f
        textAlign = Paint.Align.CENTER
    }

    private var animator: ValueAnimator? = null

    init {
        startAnimation()
    }

    private fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 4000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                animPhase = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    fun setState(newState: State) {
        state = newState
        invalidate()
    }

    fun setLevel(newLevel: Float) {
        level = newLevel
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        val cx = w / 2f
        val cy = h / 2f
        val baseRadius = min(w, h) / 3f

        when (state) {
            State.IDLE -> drawIdle(canvas, cx, cy, baseRadius)
            State.LISTENING -> drawListening(canvas, cx, cy, baseRadius)
            State.THINKING -> drawThinking(canvas, cx, cy, baseRadius)
            State.SPEAKING -> drawSpeaking(canvas, cx, cy, baseRadius)
        }

        // Draw chip label
        canvas.drawText("J.A.R.V.I.S.", cx, cy + baseRadius + 60f, textPaint)
    }

    private fun drawIdle(canvas: Canvas, cx: Float, cy: Float, r: Float) {
        // Static rings with subtle glow
        ringPaint.color = Color.parseColor("#003355")
        ringPaint.alpha = 80
        canvas.drawCircle(cx, cy, r, ringPaint)

        ringPaint.color = Color.parseColor("#002244")
        ringPaint.alpha = 60
        canvas.drawCircle(cx, cy, r * 0.7f, ringPaint)

        // Solid core
        corePaint.color = Color.parseColor("#001122")
        canvas.drawCircle(cx, cy, r * 0.35f, corePaint)

        // Glow dot
        chipPaint.color = Color.parseColor("#00D4FF")
        chipPaint.alpha = 100 + (50 * sin(animPhase * 0.05f)).toInt()
        canvas.drawCircle(cx, cy, r * 0.15f, chipPaint)
    }

    private fun drawListening(canvas: Canvas, cx: Float, cy: Float, r: Float) {
        // Pulsing outer rings
        ringPaint.color = Color.parseColor("#00D4FF")
        ringPaint.alpha = 120
        ringPaint.strokeWidth = 6f
        val pulseR = r + 10f * sin(animPhase * 0.1f)
        canvas.drawCircle(cx, cy, pulseR, ringPaint)

        ringPaint.alpha = 80
        canvas.drawCircle(cx, cy, pulseR * 0.8f, ringPaint)

        ringPaint.alpha = 50
        canvas.drawCircle(cx, cy, pulseR * 0.6f, ringPaint)

        // Core
        corePaint.color = Color.parseColor("#001a33")
        canvas.drawCircle(cx, cy, r * 0.4f, corePaint)

        // Mic dot
        chipPaint.color = Color.parseColor("#00D4FF")
        chipPaint.alpha = 200
        canvas.drawCircle(cx, cy, r * 0.12f, chipPaint)
    }

    private fun drawThinking(canvas: Canvas, cx: Float, cy: Float, r: Float) {
        // Rotating segmented ring
        ringPaint.color = Color.parseColor("#FFB300")
        ringPaint.alpha = 150
        ringPaint.strokeWidth = 5f
        val segments = 8
        for (i in 0 until segments) {
            val startAngle = animPhase + i * (360f / segments)
            val sweep = 30f
            val rect = RectF(cx - r, cy - r, cx + r, cy + r)
            canvas.drawArc(rect, startAngle, sweep, false, ringPaint)
        }

        // Pulsing inner ring
        ringPaint.color = Color.parseColor("#FFB300")
        ringPaint.alpha = 80
        val innerR = r * 0.7f + 5f * sin(animPhase * 0.15f)
        canvas.drawCircle(cx, cy, innerR, ringPaint)

        // Core
        corePaint.color = Color.parseColor("#1a1100")
        canvas.drawCircle(cx, cy, r * 0.35f, corePaint)

        // Spinning dot
        val dotAngle = animPhase * 2f
        val dotX = cx + r * 0.2f * kotlin.math.cos(Math.toRadians(dotAngle.toDouble())).toFloat()
        val dotY = cy + r * 0.2f * kotlin.math.sin(Math.toRadians(dotAngle.toDouble())).toFloat()
        chipPaint.color = Color.parseColor("#FFB300")
        chipPaint.alpha = 220
        canvas.drawCircle(dotX, dotY, r * 0.08f, chipPaint)
    }

    private fun drawSpeaking(canvas: Canvas, cx: Float, cy: Float, r: Float) {
        // Multiple expanding rings (sound waves)
        for (i in 0..3) {
            val phase = (animPhase + i * 90f) % 360f
            val ringR = r * (0.6f + 0.4f * (phase / 360f))
            val alpha = (200 * (1f - phase / 360f)).toInt()
            ringPaint.color = Color.parseColor("#00E676")
            ringPaint.alpha = alpha
            ringPaint.strokeWidth = 4f
            canvas.drawCircle(cx, cy, ringR, ringPaint)
        }

        // Outer glow ring
        ringPaint.color = Color.parseColor("#00E676")
        ringPaint.alpha = 100
        canvas.drawCircle(cx, cy, r * 1.1f, ringPaint)

        // Core pulses with level
        val coreR = r * 0.35f * (1f + level * 0.3f)
        corePaint.color = Color.parseColor("#002211")
        canvas.drawCircle(cx, cy, coreR, corePaint)

        // Level dot
        chipPaint.color = Color.parseColor("#00E676")
        chipPaint.alpha = 200
        canvas.drawCircle(cx, cy, r * 0.15f * (1f + level * 0.5f), chipPaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}