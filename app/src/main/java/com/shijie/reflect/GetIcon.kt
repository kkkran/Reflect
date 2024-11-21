package com.shijie.reflect


import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.shijie.reflect.base.BaseActivity

import java.io.File

class GetIcon(private val activity: BaseActivity) {
    private lateinit var tempFile: File
    private val resultCallback: ActivityResultCallback<ActivityResult>? = null
    private val registerLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            resultCallback?.onActivityResult(result)
        }
    private val cropResultCallback: ActivityResultCallback<ActivityResult>? = null
    private val cropLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            cropResultCallback?.onActivityResult(result)
        }

    fun pickAndCropImage(cropType: String, callback: (file: String?) -> Unit) {
        val imageIntent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
    }

}