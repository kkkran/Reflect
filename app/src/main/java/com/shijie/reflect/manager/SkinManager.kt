package com.shijie.reflect.manager

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import java.io.File

object SkinManager {
    private var currentSkin: String = "default"
    private var observers = mutableListOf<SkinObserver>()
    fun registerObserver(observer: SkinObserver): Unit {
        if (!observers.contains(observer)) {
            observers.add(observer)
        }
    }

    fun unregisterObserver(observer: SkinObserver): Unit {
        observers.remove(observer)
    }

    fun loadSkin(skin: String): Unit {
        currentSkin = skin
        notifyObservers()
    }

    fun getSkinResource(context: Context,skinName:String): Resources {
        val assetManager = AssetManager::class.java.newInstance()
        try {
            val addAssetPathMethod =
                AssetManager::class.java.getMethod("addAssetPath", String::class.java)
            val skinPath = context.filesDir.absolutePath + "/skins/$currentSkin"
            if (File(skinPath).exists()) {
                addAssetPathMethod.invoke(assetManager, skinPath)
            } else {
                return context.resources
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return context.resources
        }
        return Resources(
            assetManager,
            context.resources.displayMetrics,
            context.resources.configuration
        )
    }


    private fun notifyObservers(): Unit {
        observers.forEach { it.onSkinChanged() }
    }
}

interface SkinObserver {
    fun onSkinChanged()
}