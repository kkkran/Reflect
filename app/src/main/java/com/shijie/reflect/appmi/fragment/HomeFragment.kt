package com.shijie.reflect.appmi.fragment

import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.appbar.AppBarLayout
import com.shijie.reflect.R
import com.shijie.reflect.base.BaseFragment
import kotlin.math.abs

class HomeFragment : BaseFragment() {
    override fun initView() {
        initHeader()
    }

    private fun initHeader() {
        val appBarLayout = view?.findViewById<AppBarLayout>(R.id.home_header_appbar)
        val imageView = view?.findViewById<ImageView>(R.id.home_header_img)
        val titleText = view?.findViewById<TextView>(R.id.home_header_title)
        imageView?.alpha = 1f
        titleText?.alpha = 0f
        appBarLayout?.addOnOffsetChangedListener { _, yOffset ->
            val maxScroll = appBarLayout.totalScrollRange
            val scrollPercentage = abs(yOffset).toFloat() / maxScroll
            imageView?.alpha = 1f - scrollPercentage
            titleText?.alpha = scrollPercentage
        }

    }

    override fun getLayoutRes(): Int = R.layout.fragment_home
}