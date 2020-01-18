package com.varol.weathever.internal.di.module

import com.varol.weathever.internal.di.scope.WeatherScope
import com.varol.weathever.screen.weather.weather_home.WeatherFragment
import com.varol.weathever.screen.weather.weather_list.WeatherListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class FragmentsModule {

    @WeatherScope
    @ContributesAndroidInjector
    internal abstract fun contributeWeatherFragment(): WeatherFragment

    @WeatherScope
    @ContributesAndroidInjector
    internal abstract fun contributeWeatherListFragment(): WeatherListFragment

}