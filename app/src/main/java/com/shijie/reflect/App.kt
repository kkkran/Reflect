package com.shijie.reflect

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ShareP().init(this, "checkImage")
    }
}