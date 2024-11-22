package com.shijie.reflect.other;

import android.view.View

public class Other {

}

fun View.onClick(interval: Long = 600, onSafeClick: (View) -> Unit): Unit {
    var lastClickTime = 0L
    this.setOnClickListener { it ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > interval) {
            lastClickTime = currentTime
            onSafeClick(it)
        }
    }

}
