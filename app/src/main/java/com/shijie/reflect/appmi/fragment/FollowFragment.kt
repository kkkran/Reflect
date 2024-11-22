package com.shijie.reflect.appmi.fragment

import android.graphics.Color
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shijie.reflect.R
import com.shijie.reflect.appmi.MiRefreshLayout
import com.shijie.reflect.base.BaseFragment
import java.util.logging.Handler

class FollowFragment : BaseFragment() {
    override fun initView() {
        super.initView()
        val refreshLayout = view?.findViewById<MiRefreshLayout>(R.id.fragment_follow_refresh)
        refreshLayout?.setOnRefreshListener {
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                refreshLayout.stopRefresh()
            },2000)
        }
        val titleRoot = view?.findViewById<RelativeLayout>(R.id.base_title_root)
        val leftButton = view?.findViewById<ImageView>(R.id.base_title_left)
        val rightButton = view?.findViewById<ImageView>(R.id.base_title_right)
        val title = view?.findViewById<TextView>(R.id.base_title_center)
        titleRoot?.setBackgroundColor(Color.TRANSPARENT)
        leftButton?.visibility = View.GONE
        rightButton?.visibility = View.GONE
        title?.text = "关注"


    }

    override fun getLayoutRes(): Int = R.layout.fragment_follow
}