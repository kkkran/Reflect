package com.shijie.reflect

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.shijie.reflect.service.DownloadService

object DownloadManager {
    fun startDownload(context: Context): Unit {
        val serviceIntent = Intent(context, DownloadService::class.java)
        val connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as? DownloadService.DownloadBinder
                binder?.startDownload(
                    callback = { progress ->
                        NotificationHelper.showProgressNotification(context, progress)
                    },
                    onComplete = { filePath ->
                        NotificationHelper.showCompletionNotification(context, filePath)
                    }
                )
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                context.unbindService(this)
            }
        }
        context.bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
    }
}