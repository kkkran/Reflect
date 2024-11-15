package com.shijie.reflect

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import com.github.chrisbanes.photoview.PhotoView

class MainCase {

    fun cropCircularImage(context: Context, photoView: PhotoView, cropArea: RectF): String {
        photoView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(photoView.drawingCache)
        photoView.isDrawingCacheEnabled = false

        val endBitmap = Bitmap.createBitmap(
            bitmap,
            cropArea.left.toInt(),
            cropArea.top.toInt(),
            cropArea.width().toInt(),
            cropArea.height().toInt()
        )

        val filePath = FileUtils.saveBitmapToTempFile(context, endBitmap, "temp_image.png")
        return filePath ?: ""

    }

    fun cropRectImage(
        context: Context,
        photoView: PhotoView,
        cropArea: RectF,
        overlayView: ResizeableOverlayView
    ): String {
        photoView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(photoView.drawingCache)
        photoView.isDrawingCacheEnabled = false

        val scaleX = bitmap.width / overlayView.width.toFloat()
        val scaleY = bitmap.height / overlayView.height.toFloat()
        val left = (cropArea.left * scaleX).toInt().coerceAtLeast(0)
        val top = (cropArea.top * scaleY).toInt().coerceAtLeast(0)
        val right = (cropArea.right * scaleX).toInt().coerceAtMost(bitmap.width)
        val bottom = (cropArea.bottom * scaleY).toInt().coerceAtMost(bitmap.height)


        val endBitmap = Bitmap.createBitmap(
            bitmap,
            left, top, right - left, bottom - top
        )

        val filePath =
            FileUtils.saveBitmapToTempFile(context, endBitmap, "temp_background_image.png")
        return filePath ?: ""

    }

    fun cropBitmapToCircle(bitmap: Bitmap): Bitmap {
        val size = minOf(bitmap.width, bitmap.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val paint = Paint().apply {
            this.isAntiAlias = true
            this.shader = shader
        }
        val radius = size / 2f
        canvas.drawCircle(radius, radius, radius, paint)
        return output
    }
}