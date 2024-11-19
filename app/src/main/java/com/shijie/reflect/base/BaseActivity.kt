package com.shijie.reflect.base

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.shijie.reflect.R
import com.shijie.reflect.SimpleToast

abstract class BaseActivity(val needImmersive: Boolean) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(getLayoutResId())
        if (needImmersive) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                v.post {
                    val statusBarColor = getDominantColorFromView(v, 0, 0, 200, systemBars.top)
                    setStatusBarColor(this, statusBarColor)
                    // val inverseColor = getInverseColor(statusBarColor)
                    setStatusBarIconColor(this, isColorLight(statusBarColor))
                }
                insets
            }
        }
    }

    private fun isColorLight(inverseColor: Int): Boolean {
        val r = Color.red(inverseColor)
        val g = Color.green(inverseColor)
        val b = Color.blue(inverseColor)
        val endColor = 0.299 * r + 0.587 * g + 0.114 * b
        return endColor > 128
    }

    private fun setStatusBarColor(baseActivity: BaseActivity, color: Int) {
        val window = baseActivity.window
        window.statusBarColor = color
    }

    private fun getInverseColor(statusBarColor: Int): Int {
        val r = 255 - Color.red(statusBarColor)
        val g = 255 - Color.green(statusBarColor)
        val b = 255 - Color.blue(statusBarColor)
        return Color.rgb(r, g, b)
    }

    fun getDominantColorFromView(view: View, x: Int, y: Int, width: Int, height: Int): Int {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        val croppedBitmap = Bitmap.createBitmap(bitmap, x, y, width, height)
        val dominantColor = getDominantColor(croppedBitmap)
        Log.d("BaseActivity", "dominantColor:${dominantColor}")
        croppedBitmap.recycle()
        bitmap.recycle()
        return dominantColor
    }

    private fun getDominantColor(croppedBitmap: Bitmap, sampleStep: Int = 5): Int {
        val colorCountMap = mutableMapOf<Int, Int>()
        for (x in 0 until croppedBitmap.width step sampleStep) {
            for (y in 0 until croppedBitmap.height step sampleStep) {
                val pixelColor = croppedBitmap.getPixel(x, y)
                if (Color.alpha(pixelColor) < 255) continue
                colorCountMap[pixelColor] = colorCountMap.getOrDefault(pixelColor, 0) + 1
            }
        }
        return colorCountMap.maxByOrNull { it.value }?.key ?: Color.WHITE

    }

    private fun setStatusBarIconColor(activity: BaseActivity, isDark: Boolean): Unit {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.insetsController?.setSystemBarsAppearance(
                if (isDark) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            if (isDark) {
                activity.window.decorView.systemUiVisibility =
                    activity.window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                activity.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }

    abstract fun getLayoutResId(): Int
}