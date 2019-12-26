package com.varol.weathever.screen.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.varol.weathever.R
import com.varol.weathever.base.BaseBindingActivity
import com.varol.weathever.databinding.ActivityMainBinding

class MainActivity : BaseBindingActivity<MainViewModel, ActivityMainBinding>() {
    override val layoutId get() = R.layout.activity_main

    companion object {
        const val NAV_DESTINATION_LOGIN = R.id.graph_main

        fun launch(activity: AppCompatActivity) =
            activity.apply {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }
}
