package com.varol.weathever.internal.di.component

import com.varol.weathever.internal.di.DaggerApplication
import com.varol.weathever.internal.di.module.ActivitiesModule
import com.varol.weathever.internal.di.module.AppModule
import com.varol.weathever.internal.di.module.FragmentsModule
import com.varol.weathever.internal.di.module.NetworkModule
import com.varol.weathever.internal.di.viewmodel.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivitiesModule::class,
        FragmentsModule::class,
        ViewModelModule::class,
        AppModule::class,
        NetworkModule::class
    ]
)
internal interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<DaggerApplication>()
}
