package com.varol.weathever.application

import com.facebook.stetho.Stetho
import com.varol.weathever.internal.di.DaggerApplication

class MainApplication : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
    }
}