package com.shijie.reflect

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.github.chrisbanes.photoview.PhotoView
import kotlin.math.min

class OverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint().apply {
        color = Color.parseColor("#80000000")
    }
    private val transparentPaint = Paint().apply {
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
    private val framePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }
    private var cropRadius = 200f
    private var cropCenterX = 0f
    private var cropCenterY = 0f

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        cropRadius = if (min(width, height) == width) width / 2f else height / 2f
        cropCenterX = width / 2f
        cropCenterY = height / 2f

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        canvas.drawCircle(cropCenterX, cropCenterY, cropRadius, transparentPaint)
        canvas.drawCircle(cropCenterX, cropCenterY, cropRadius, framePaint)

    }

    fun getCropArea(): RectF {
        return RectF(
            cropCenterX - cropRadius,
            cropCenterY - cropRadius,
            cropCenterX + cropRadius,
            cropCenterY + cropRadius
        )
    }
}