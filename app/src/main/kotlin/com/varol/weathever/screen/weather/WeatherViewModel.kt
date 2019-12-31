package com.varol.weathever.screen.weather

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.varol.weathever.base.BaseAndroidViewModel
import com.varol.weathever.data.entity.WeatherViewEntity
import com.varol.weathever.domain.GetLocationWeatherUseCase
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    context: Context,
    private val weatherByLoc: GetLocationWeatherUseCase
) : BaseAndroidViewModel(context) {

    val currentLocWeather = MutableLiveData<WeatherViewEntity>()

    fun getWeatherByLocation(lat: Double, lon: Double) {
        disposables.add(
            weatherByLoc.getWeatherByLocation(lat, lon)
                .subscribe { value -> value.either(::handleFailure, ::handleSuccessGetLocWeather) }
        )
    }

    private fun handleSuccessGetLocWeather(weatherViewEntity: WeatherViewEntity) {
        currentLocWeather.postValue(weatherViewEntity)
    }
}