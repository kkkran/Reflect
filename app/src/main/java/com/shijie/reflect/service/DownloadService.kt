package com.shijie.reflect.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper

class DownloadService : Service() {
    private val binder = DownloadBinder()
    private val handler = Handler(Looper.getMainLooper())
    override fun onBind(p0: Intent?): IBinder = binder
    inner class DownloadBinder : Binder() {
        fun startDownload(callback: (Int) -> Unit, onComplete: (String) -> Unit): Unit {
            downloadThemeInfo(callback, onComplete)
        }
    }

    private fun downloadThemeInfo(callback: (Int) -> Unit, onComplete: (String) -> Unit): Unit {
        Thread {
            for (progress in 1..100) {
                Thread.sleep(100)
                handler.post { callback(progress) }
            }
            handler.post { onComplete("file://path/to/download/file") }
        }.start()
    }
}