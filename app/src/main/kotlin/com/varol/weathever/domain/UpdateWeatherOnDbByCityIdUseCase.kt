package com.varol.weathever.domain

import com.varol.weathever.data.entity.WeatherViewEntity
import com.varol.weathever.data.repository.WeatherRepository
import io.reactivex.Single
import javax.inject.Inject

class UpdateWeatherOnDbUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    fun updateWeatherOnDb(
        weatherViewEntity: WeatherViewEntity
    ): Single<Int> {
        return weatherRepository.updateWeatherOnDb(weatherViewEntity)
    }
}