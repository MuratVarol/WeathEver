package com.varol.weathever.domain

import com.varol.weathever.data.entity.WeatherViewEntity
import com.varol.weathever.data.repository.WeatherRepository
import io.reactivex.Single
import javax.inject.Inject

class DeleteWeatherFromDbUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    fun deleteWeatherFromDbByCityId(
        cityId: Long
    ): Single<Int> {
        return weatherRepository.deleteWeatherFromDbByCityId(cityId)
    }

    fun deleteWeatherFromDb(
        weather: WeatherViewEntity
    ): Single<Int> {
        return weatherRepository.deleteWeatherFromDb(weather)
    }
}