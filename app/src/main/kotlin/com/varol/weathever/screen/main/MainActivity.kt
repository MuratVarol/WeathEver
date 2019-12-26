package com.varol.weathever.screen.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.varol.weathever.R
import com.varol.weathever.base.BaseBindingActivity
import com.varol.weathever.databinding.ActivityMainBinding

const val TRANSLUCENT_COLOR = 0x19000000

class MainActivity : BaseBindingActivity<MainViewModel, ActivityMainBinding>() {
    override val layoutId get() = R.layout.activity_main
    val navController: NavController by lazy { findNavController(R.id.navHostFragment) }

    companion object {

        fun launch(activity: AppCompatActivity) =
            activity.apply {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTranslucent()
    }

    private fun setTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = TRANSLUCENT_COLOR
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

}
