package com.darrenai.jarvis.ui.face

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

/**
 * Native Arc Reactor face visualizer — circuit-board inspired.
 *
 * Layers (back to front):
 * 1. Faint trace grid radiating from the center chip.
 * 2. Rotating dashed orbit rings (opposite directions) + orbiting particles.
 * 3. 48-bar waveform ring driven by voice level + animation phase.
 * 4. Glowing core with state color.
 * 5. Center chip plate labeled JARVIS + state text below.
 *
 * States mirror ai-visualizer: idle | listening | thinking | speaking.
 * No server needed — runs entirely on-device.
 */
class ArcReactorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    enum class State { IDLE, LISTENING, THINKING, SPEAKING }

    private var state = State.IDLE
    private var targetLevel = 0f
    private var smoothLevel = 0f
    private var rotatePhase = 0f
    private var pulsePhase = 0f

    private data class Trace(val angle: Float, val lenFrac: Float, val kink: Float)
    private val traces: List<Trace> = List(36) { i ->
        Trace(
            angle = i * 10f + Random.nextFloat() * 4f,
            lenFrac = 0.55f + Random.nextFloat() * 0.4f,
            kink = (Random.nextFloat() - 0.5f) * 30f
        )
    }
    private data class Particle(val orbitFrac: Float, val speed: Float, val sizeFrac: Float, val dir: Float)
    private val particles: List<Particle> = List(10) {
        Particle(
            orbitFrac = 0.78f + Random.nextFloat() * 0.3f,
            speed = 20f + Random.nextFloat() * 40f,
            sizeFrac = 0.012f + Random.nextFloat() * 0.014f,
            dir = if (Random.nextBoolean()) 1f else -1f
        )
    }

    private val tracePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE; strokeWidth = 2f }
    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }
    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE; strokeCap = Paint.Cap.ROUND }
    private val corePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val chipPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
    }
    private val statePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
    }

    private var rotateAnimator: ValueAnimator? = null
    private var pulseAnimator: ValueAnimator? = null

    init {
        startAnimation()
    }

    private fun startAnimation() {
        rotateAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 9000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                rotatePhase = it.animatedValue as Float
                // Ease the level toward its target every frame.
                smoothLevel += (targetLevel - smoothLevel) * 0.12f
                invalidate()
            }
            start()
        }
        pulseAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 1600
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                pulsePhase = it.animatedValue as Float
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
        targetLevel = newLevel.coerceIn(0f, 1f)
    }

    private data class Palette(val main: Int, val dim: Int, val accent: Int, val name: String)

    private fun palette(): Palette = when (state) {
        State.IDLE -> Palette(
            main = Color.parseColor("#00D4FF"),
            dim = Color.parseColor("#003355"),
            accent = Color.parseColor("#007A99"),
            name = "IDLE"
        )
        State.LISTENING -> Palette(
            main = Color.parseColor("#00E5FF"),
            dim = Color.parseColor("#005577"),
            accent = Color.parseColor("#00D4FF"),
            name = "LISTENING"
        )
        State.THINKING -> Palette(
            main = Color.parseColor("#FFB300"),
            dim = Color.parseColor("#6B4A00"),
            accent = Color.parseColor("#E7C368"),
            name = "THINKING"
        )
        State.SPEAKING -> Palette(
            main = Color.parseColor("#00E676"),
            dim = Color.parseColor("#00552B"),
            accent = Color.parseColor("#3DDC84"),
            name = "SPEAKING"
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        if (w <= 0f || h <= 0f) return
        val cx = w / 2f
        val cy = h / 2f - h * 0.03f
        val r = min(w, h) / 3.1f
        val pal = palette()
        val pulse = 0.5f + 0.5f * sin(Math.toRadians(pulsePhase.toDouble())).toFloat()

        drawTraces(canvas, cx, cy, r, pal)
        drawOrbitRings(canvas, cx, cy, r, pal)
        drawParticles(canvas, cx, cy, r, pal)
        drawWaveformRing(canvas, cx, cy, r, pal, pulse)
        drawCore(canvas, cx, cy, r, pal, pulse)
        drawChip(canvas, cx, cy, r, pal)
    }

    private fun drawTraces(canvas: Canvas, cx: Float, cy: Float, r: Float, pal: Palette) {
        tracePaint.color = pal.dim
        tracePaint.alpha = if (state == State.IDLE) 60 else 110
        tracePaint.strokeWidth = 2f
        for (t in traces) {
            val rad = Math.toRadians(t.angle.toDouble())
            val x0 = cx + cos(rad).toFloat() * r * 0.42f
            val y0 = cy + sin(rad).toFloat() * r * 0.42f
            val xm = cx + cos(rad).toFloat() * r * (0.42f + t.lenFrac * 0.35f)
            val ym = cy + sin(rad).toFloat() * r * (0.42f + t.lenFrac * 0.35f) + t.kink * 0.15f
            val x1 = cx + cos(rad).toFloat() * r * (0.42f + t.lenFrac)
            val y1 = cy + sin(rad).toFloat() * r * (0.42f + t.lenFrac) + t.kink * 0.3f
            canvas.drawLine(x0, y0, xm, ym, tracePaint)
            canvas.drawLine(xm, ym, x1, y1, tracePaint)
            // Node dot at the trace end, lit in the active color.
            dotPaint.color = pal.accent
            dotPaint.alpha = if (state == State.IDLE) 70 else 160
            canvas.drawCircle(x1, y1, 3f, dotPaint)
        }
    }

    private fun drawOrbitRings(canvas: Canvas, cx: Float, cy: Float, r: Float, pal: Palette) {
        val rect1 = RectF(cx - r * 1.02f, cy - r * 1.02f, cx + r * 1.02f, cy + r * 1.02f)
        ringPaint.color = pal.main
        ringPaint.alpha = 130
        ringPaint.strokeWidth = 3f
        ringPaint.pathEffect = DashPathEffect(floatArrayOf(26f, 18f), rotatePhase)
        canvas.drawArc(rect1, 0f, 360f, false, ringPaint)

        val rect2 = RectF(cx - r * 0.86f, cy - r * 0.86f, cx + r * 0.86f, cy + r * 0.86f)
        ringPaint.color = pal.accent
        ringPaint.alpha = 100
        ringPaint.strokeWidth = 2f
        ringPaint.pathEffect = DashPathEffect(floatArrayOf(12f, 22f), -rotatePhase * 1.6f)
        canvas.drawArc(rect2, 0f, 360f, false, ringPaint)
        ringPaint.pathEffect = null
    }

    private fun drawParticles(canvas: Canvas, cx: Float, cy: Float, r: Float, pal: Palette) {
        dotPaint.color = pal.main
        for (p in particles) {
            val ang = Math.toRadians((rotatePhase * p.dir * p.speed / 30f).toDouble())
            val or_ = r * p.orbitFrac
            val x = cx + cos(ang).toFloat() * or_
            val y = cy + sin(ang).toFloat() * or_
            dotPaint.alpha = 140
            canvas.drawCircle(x, y, r * p.sizeFrac, dotPaint)
        }
    }

    private fun drawWaveformRing(canvas: Canvas, cx: Float, cy: Float, r: Float, pal: Palette, pulse: Float) {
        val bars = 48
        barPaint.color = pal.main
        barPaint.strokeWidth = 5f
        val baseR = r * 0.60f
        for (i in 0 until bars) {
            val ang = Math.toRadians((i * 360f / bars).toDouble())
            val wave = sin(Math.toRadians((rotatePhase * 2f + i * 360f / bars * 2f).toDouble())).toFloat()
            val energy = when (state) {
                State.IDLE -> 0.12f + 0.06f * wave
                State.LISTENING -> 0.25f + 0.20f * pulse + 0.08f * wave
                State.THINKING -> 0.30f + 0.15f * sin(Math.toRadians((pulsePhase + i * 15f).toDouble())).toFloat()
                State.SPEAKING -> 0.25f + smoothLevel * 0.9f * (0.5f + 0.5f * wave)
            }
            val len = (r * 0.10f * energy.coerceIn(0.05f, 1.2f)).coerceAtLeast(3f)
            val x0 = cx + cos(ang).toFloat() * baseR
            val y0 = cy + sin(ang).toFloat() * baseR
            val x1 = cx + cos(ang).toFloat() * (baseR + len)
            val y1 = cy + sin(ang).toFloat() * (baseR + len)
            barPaint.alpha = (110 + 120 * energy.coerceIn(0f, 1f)).toInt().coerceIn(0, 255)
            canvas.drawLine(x0, y0, x1, y1, barPaint)
        }
    }

    private fun drawCore(canvas: Canvas, cx: Float, cy: Float, r: Float, pal: Palette, pulse: Float) {
        // Outer glow layers.
        corePaint.color = pal.main
        corePaint.alpha = 18
        canvas.drawCircle(cx, cy, r * 0.52f, corePaint)
        corePaint.alpha = 30
        canvas.drawCircle(cx, cy, r * 0.44f, corePaint)
        // Core body, breathing slightly.
        val breathe = 1f + 0.04f * pulse + smoothLevel * 0.10f
        corePaint.color = Color.parseColor("#04121A")
        corePaint.alpha = 255
        canvas.drawCircle(cx, cy, r * 0.36f * breathe, corePaint)
        // Hot center dot.
        dotPaint.color = pal.main
        dotPaint.alpha = 220
        canvas.drawCircle(cx, cy, r * (0.10f + 0.05f * pulse + smoothLevel * 0.06f), dotPaint)
    }

    private fun drawChip(canvas: Canvas, cx: Float, cy: Float, r: Float, pal: Palette) {
        val chipW = r * 0.92f
        val chipH = r * 0.30f
        val top = cy + r * 1.28f
        chipPaint.color = Color.parseColor("#0A1418")
        chipPaint.alpha = 235
        val rect = RectF(cx - chipW / 2f, top, cx + chipW / 2f, top + chipH)
        canvas.drawRoundRect(rect, 10f, 10f, chipPaint)
        ringPaint.color = pal.main
        ringPaint.alpha = 170
        ringPaint.strokeWidth = 2f
        canvas.drawRoundRect(rect, 10f, 10f, ringPaint)
        labelPaint.color = pal.main
        labelPaint.alpha = 255
        labelPaint.textSize = r * 0.13f
        canvas.drawText("J.A.R.V.I.S.", cx, top + chipH * 0.44f, labelPaint)
        statePaint.color = pal.accent
        statePaint.alpha = 220
        statePaint.textSize = r * 0.095f
        canvas.drawText("· ${pal.name} ·", cx, top + chipH * 0.74f, statePaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        rotateAnimator?.cancel()
        pulseAnimator?.cancel()
    }
}
