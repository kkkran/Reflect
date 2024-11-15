package com.shijie.reflect

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    fun saveBitmapToTempFile(
        context: Context,
        bitmap: Bitmap,
        fileName: String
    ): String? {
        val tempFile = File(context.cacheDir, fileName)
        return try {
            FileOutputStream(tempFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            tempFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun loadBitmapFromFile(fileName: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun removeFile(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists() && file.delete()
    }
}