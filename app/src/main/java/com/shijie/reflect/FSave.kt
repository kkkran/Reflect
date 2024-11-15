package com.shijie.reflect

object FSave {
    var backgroundPath: String
        get() = ShareP().getString("background", "")
        set(value) {
            ShareP().putString("background", value)
        }
    var iconPath: String
        get() = ShareP().getString("icon", "")
        set(value) {
            ShareP().putString("icon", value)
        }
}