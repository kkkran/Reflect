package com.shijie.reflect

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    val mainCase: MainCase by lazy {
        MainCase()
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
        private const val REQUEST_BACKGROUND_PICK = 3
        private const val REQUEST_IMAGE_CROP = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            insets
        }

        val click = findViewById<TextView>(R.id.main_text)
        val click2 = findViewById<TextView>(R.id.main_text2)
        val background = findViewById<ImageView>(R.id.main_background)
        val icon = findViewById<ImageView>(R.id.main_image)
        if (FSave.backgroundPath.isNotBlank()) {
            background.setImageBitmap(FileUtils.loadBitmapFromFile(FSave.backgroundPath))
        }
        if (FSave.iconPath.isNotBlank()) {
            icon.setImageBitmap(FileUtils.loadBitmapFromFile(FSave.iconPath))
        }
        click.setOnClickListener { v ->
            pickImageFromGallery()
        }
        click2.setOnClickListener { v ->
            pickBackgroundFromGallery()
        }
    }

    fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    fun pickBackgroundFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_BACKGROUND_PICK)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_PICK -> {
                if (resultCode == RESULT_OK && data != null) {
                    val selectedImageUri = data.data
                    val cropIntent = Intent(this, OtherActivity::class.java).apply {
                        putExtra("imageUri", selectedImageUri.toString())
                        putExtra("pickWhat", "image")
                    }
                    startActivityForResult(cropIntent, REQUEST_IMAGE_CROP)
                }
            }

            REQUEST_BACKGROUND_PICK -> {
                if (resultCode == RESULT_OK && data != null) {
                    val selectedImageUri = data.data
                    val cropIntent = Intent(this, OtherActivity::class.java).apply {
                        putExtra("imageUri", selectedImageUri.toString())
                        putExtra("pickWhat", "background")
                    }
                    startActivityForResult(cropIntent, REQUEST_IMAGE_CROP)
                }
            }

            REQUEST_IMAGE_CROP -> {
                if (resultCode == RESULT_OK && data != null) {
                    val pickWhat = data.getStringExtra("pickWhat")
                    val croppedImagePath = data.getStringExtra("croppedImage") ?: ""
                    if (pickWhat == "image") {
                        FSave.iconPath = croppedImagePath
                    } else if (pickWhat == "background") {
                        FSave.backgroundPath = croppedImagePath
                    }
                    SimpleToast(this).createShortToast(content = croppedImagePath)
                    val croppedBitmap = FileUtils.loadBitmapFromFile(croppedImagePath)
                    var endBitmap: Bitmap? = croppedBitmap
                    when (pickWhat) {
                        "image" -> {
                            endBitmap = mainCase.cropBitmapToCircle(croppedBitmap!!)
                            findViewById<ImageView>(R.id.main_image).setImageBitmap(endBitmap)
                        }

                        "background" -> {
                            findViewById<ImageView>(R.id.main_background).setImageBitmap(endBitmap)
                        }
                    }
//                    FileUtils.removeFile(croppedImagePath)
                }
            }
        }
    }
}