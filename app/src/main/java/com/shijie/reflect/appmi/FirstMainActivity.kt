package com.shijie.reflect.appmi

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.appbar.AppBarLayout
import com.shijie.reflect.R
import com.shijie.reflect.SimpleToast
import com.shijie.reflect.base.BaseActivity
import kotlin.math.abs
import kotlin.math.max

class FirstMainActivity : BaseActivity(false) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        initHeader()
        initFragment()
        initViews()
    }

    private fun initHeader() {
        val appBarLayout = findViewById<AppBarLayout>(R.id.first_main_appbar)
        val imageView = findViewById<ImageView>(R.id.first_main_title)
        val titleText = findViewById<TextView>(R.id.first_main_txt)
        imageView.alpha = 1f
        titleText.alpha = 0f
        appBarLayout.addOnOffsetChangedListener { _, yOffset ->
            val maxScroll = appBarLayout.totalScrollRange
            val scrollPercentage = abs(yOffset).toFloat() / maxScroll
            imageView.alpha = 1f - scrollPercentage
            titleText.alpha = scrollPercentage
        }
    }

    private fun initViews() {

    }

    private fun initFragment() {

    }

    private fun init(): Unit {

    }

    override fun getLayoutResId(): Int = R.layout.activity_first_main
    override fun getMainView(): Int {
        return R.id.main
    }
}