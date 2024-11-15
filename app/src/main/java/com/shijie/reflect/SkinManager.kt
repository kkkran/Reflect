package com.shijie.reflect

import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable

object SkinManager {
    private var currentSkinResources: Resources? = null

    fun applySkin(skinPath: String) {
        val assetManager = AssetManager::class.java.newInstance()
        val addAssetPathMode = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
        addAssetPathMode.invoke(assetManager, skinPath)
        currentSkinResources = Resources(
            assetManager, Resources.getSystem().displayMetrics,
            Resources.getSystem().configuration
        )

        fun getColor(resId: Int): Int =
            currentSkinResources?.getColor(resId, null) ?: Resources.getSystem().getColor(resId)

        fun getDrawable(resId: Int): Drawable? =
            currentSkinResources?.getDrawable(resId, null) ?: Resources.getSystem()
                .getDrawable(resId)
    }
}