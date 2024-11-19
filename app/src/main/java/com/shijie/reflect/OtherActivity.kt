package com.shijie.reflect

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.chrisbanes.photoview.PhotoView
import com.shijie.reflect.R
import java.io.ByteArrayOutputStream

class OtherActivity : AppCompatActivity() {
    var imageUri: Uri ?=null
    private lateinit var photoView: PhotoView
    private lateinit var photoViewBackground: PhotoView
    private lateinit var overLayView: OverlayView
    private lateinit var overLayViewBackground: ResizeableOverlayView
    private lateinit var pickWhat: String

    val mainCase: MainCase by lazy {
        MainCase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_other)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        NotificationHelper.createNotificationChannel(this)
        imageUri = Uri.parse(intent.getStringExtra("imageUri"))?:null
        pickWhat = intent.getStringExtra("pickWhat") ?: ""
        photoView = findViewById(R.id.other_photoView)
        photoViewBackground = findViewById(R.id.other_photoView_background)
        overLayView = findViewById(R.id.other_overlay)
        overLayViewBackground = findViewById(R.id.other_overlay_background)
        when (pickWhat) {
            "image" -> {
                photoViewBackground.visibility = View.GONE
                overLayViewBackground.visibility = View.GONE
                photoView.setImageURI(imageUri)
            }

            "background" -> {
                photoView.visibility = View.GONE
                overLayView.visibility = View.GONE
                photoViewBackground.setImageURI(imageUri)
            }
        }

        findViewById<ImageView>(R.id.base_title_left).setOnClickListener { v ->
            DownloadManager.startDownload(this)
        }

        findViewById<ImageView>(R.id.base_title_right).setOnClickListener { v ->
            var croppedBitmapPath: String
            when (pickWhat) {
                "image" -> {
                    croppedBitmapPath =
                        mainCase.cropCircularImage(this, photoView, overLayView.getCropArea())
                    val resultIntent = Intent().apply {
                        putExtra("pickWhat", "image")
                        putExtra("croppedImage", croppedBitmapPath)
                    }
                    setResult(RESULT_OK, resultIntent)
                }

                "background" -> {
                    croppedBitmapPath = mainCase.cropRectImage(
                        this,
                        photoViewBackground,
                        overLayViewBackground.getCropRect(), overLayViewBackground
                    )
                    val resultIntent = Intent().apply {
                        putExtra("pickWhat", "background")
                        putExtra("croppedImage", croppedBitmapPath)
                    }
                    setResult(RESULT_OK, resultIntent)
                }
            }


            finish()
        }


    }

    fun bitmapToByteArray(bitmap: Bitmap?): ByteArray {
        val steam = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, steam)
        return steam.toByteArray()
    }
}