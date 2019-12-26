package com.varol.weathever.internal.di.module

import com.varol.weathever.internal.di.scope.MainScope
import com.varol.weathever.screen.main.MainActivity
import com.varol.weathever.screen.main.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivitiesModule {

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun contributeMainActivity(): MainActivity
}