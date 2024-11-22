package com.shijie.reflect.appmi

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.shijie.reflect.R
import com.shijie.reflect.SimpleToast
import com.shijie.reflect.appmi.fragment.FollowFragment
import com.shijie.reflect.appmi.fragment.HomeFragment
import com.shijie.reflect.base.BaseActivity
import com.shijie.reflect.base.BaseFragment
import com.shijie.reflect.other.onClick
import kotlin.math.abs
import kotlin.math.max

class FirstMainActivity : BaseActivity(false) {
    private lateinit var homeButton: LinearLayout
    private lateinit var followButton: LinearLayout
    private lateinit var chatsButton: LinearLayout
    private lateinit var meButton: LinearLayout
    private lateinit var currentFragment: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        initViews()
        initFragment(savedInstanceState)

    }


    private fun initViews() {
        homeButton = findViewById(R.id.main_fragment_home)
        meButton = findViewById(R.id.main_fragment_me)
        followButton = findViewById(R.id.main_fragment_follow)
        chatsButton = findViewById(R.id.main_fragment_chats)
    }

    private fun initFragment(bundle: Bundle?) {
        if (bundle == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_fragment_container, HomeFragment())
                .commit()
            selectedHome()
        }
        homeButton.onClick {

            if (currentFragment == "homeFragment") return@onClick
            switchFragment(HomeFragment())
            currentFragment = "homeFragment"
            checkIconAndText()
        }
        followButton.onClick {
            if (currentFragment == "followFragment") return@onClick
            switchFragment(FollowFragment())
            currentFragment = "followFragment"
            checkIconAndText()
        }

    }

    private fun selectedHome() {
        currentFragment = "homeFragment"
    }

    private fun checkIconAndText() {

    }

    private fun switchFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun init(): Unit {

    }

    override fun getLayoutResId(): Int = R.layout.activity_first_main
    override fun getMainView(): Int {
        return R.id.main
    }
}