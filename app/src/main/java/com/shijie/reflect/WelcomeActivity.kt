package com.shijie.reflect

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shijie.reflect.base.BaseActivity

class WelcomeActivity : BaseActivity(true) {
    private lateinit var recycleView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recycleView = findViewById(R.id.welcome_recycle)
        val activities = getActivitiesFromManifest()
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = SimpleAdapter(activities)
            .setLayout(R.layout.view_activity_intent)
            .setBinder { view, activityInfo, i ->
                view.findViewById<TextView>(R.id.view_activity_intent_txt).let { v ->
                    v.text = activityInfo.name
                    v.setOnClickListener {
                        try {
                            val intent = Intent().apply {
                                setClassName(this@WelcomeActivity, activityInfo.name)
                            }
                            startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

            }

    }

    override fun getLayoutResId(): Int = R.layout.activity_welcome

    private fun getActivitiesFromManifest(): List<ActivityInfo> {
        val activityList = mutableListOf<ActivityInfo>()
        try {
            val packageManager = packageManager
            val packageInfo =
                packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            val activities = packageInfo.activities
            activities?.forEach { activity ->
                val labelRes = activity.labelRes
                val label = if (labelRes != 0) getString(labelRes) else activity.name
                if (activity.name != OtherActivity::class.java.name) {
                    activityList.add(ActivityInfo(activity.name, label))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return activityList
    }

    data class ActivityInfo(val name: String, val label: String)
}