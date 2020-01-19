package com.varol.weathever.application

import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.varol.weathever.internal.di.DaggerApplication

class MainApplication : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        Stetho.initializeWithDefaults(this)
    }
}