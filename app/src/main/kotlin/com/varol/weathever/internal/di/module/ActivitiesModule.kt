package com.varol.weathever.internal.di.module

import com.varol.weathever.internal.di.scope.MainScope
import com.varol.weathever.internal.di.scope.SplashScope
import com.varol.weathever.screen.main.MainActivity
import com.varol.weathever.screen.main.MainModule
import com.varol.weathever.screen.splash.SplashActivity
import com.varol.weathever.screen.splash.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivitiesModule {

    @SplashScope
    @ContributesAndroidInjector
    internal abstract fun contributeSplashActivity(): SplashActivity

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun contributeMainActivity(): MainActivity
}