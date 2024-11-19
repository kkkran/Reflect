package com.shijie.reflect

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView

import android.widget.Toast

class SimpleToast(private val context: Context) : Toast(context) {
    private lateinit var toast: Toast
    private lateinit var textview: TextView

    init {
        val inflater = LayoutInflater.from(context)
        val baseRoot = inflater.inflate(R.layout.view_simple_toast, null)
        textview = baseRoot.findViewById(R.id.view_simple_toast_content)
        toast = Toast(context)
        toast.let { to ->
            to.view = baseRoot
        }
    }

    fun createLongToast(marginBottom: Int = 50, content: String = "提示"): Unit {
        textview.text = content.toString()
        toast.duration = LENGTH_LONG
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, -marginBottom)
        toast.show()
    }

    fun createShortToast(marginBottom: Int = 50, content: String = "提示"): Unit {
        textview.text = content.toString()
        toast.duration = LENGTH_SHORT
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, -marginBottom)
        toast.show()
    }

}