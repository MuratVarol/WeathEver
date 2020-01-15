package com.varol.weathever.domain

import com.varol.weathever.data.entity.WeatherViewEntity
import com.varol.weathever.data.repository.WeatherRepository
import io.reactivex.Single
import javax.inject.Inject

class SveCurrentWeatherToDbUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    fun saveWeatherToDb(
        weatherViewEntity: WeatherViewEntity
    ): Single<Long> {
        return weatherRepository.saveCurrentWeatherToDb(weatherViewEntity)
    }
}