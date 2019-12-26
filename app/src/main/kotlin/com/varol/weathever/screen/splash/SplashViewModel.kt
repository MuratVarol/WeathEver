package com.varol.weathever.screen.splash

import android.app.Application
import android.content.Context
import com.varol.weathever.base.BaseAndroidViewModel
import javax.inject.Inject

class SplashViewModel @Inject constructor(context: Context) :
    BaseAndroidViewModel(context.applicationContext as Application)
