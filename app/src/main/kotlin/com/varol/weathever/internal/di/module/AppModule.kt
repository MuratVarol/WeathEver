package com.varol.weathever.internal.di.module

import android.content.Context
import com.varol.weathever.internal.di.DaggerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    internal fun provideContext(application: DaggerApplication): Context =
        application.applicationContext
}