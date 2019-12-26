package com.varol.weathever.screen.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.varol.weathever.screen.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainActivity.launch(this)
    }
}