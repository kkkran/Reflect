package com.shijie.reflect

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class ResizeableOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val overlayPaint = Paint().apply {
        color = Color.parseColor("#99000000")
    }
    private val borderPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }
    private val transparentPaint = Paint().apply {
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private var cropRect = RectF(100f, 100f, 1000f, 1900f)
    private var lastX = 0f
    private var lastY = 0f
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlayPaint)
        canvas.drawRect(cropRect, borderPaint)
        canvas.drawRect(cropRect, transparentPaint)
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - lastX
                val dy = event.y - lastY
                cropRect.offset(dx, dy)
                lastX = event.x
                lastY = event.y
                invalidate()
            }
        }
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(
                    "cropRect",
                    "dispatchTouchEvent: ${cropRect.left},${cropRect.right},${cropRect.top},${cropRect.bottom}"
                )
                Log.d(
                    "down",
                    "down: ${event.x},${event.y}"
                )
                if (event.x >cropRect.left && event.x <cropRect.right&&event.y > cropRect.top && event.y < cropRect.bottom) return false
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun getCropRect(): RectF {
        return cropRect
    }

}