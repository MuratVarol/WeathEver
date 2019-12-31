package com.varol.weathever.domain

import com.varol.weathever.data.entity.WeatherViewEntity
import com.varol.weathever.data.remote.Either
import com.varol.weathever.data.repository.WeatherRepository
import com.varol.weathever.internal.util.Failure
import io.reactivex.Single
import javax.inject.Inject

class GetLocationWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    fun getWeatherByLocation(
        lat: Double,
        long: Double
    ): Single<Either<Failure, WeatherViewEntity>> {
        return weatherRepository.getWeatherByLocation(lat, long)
    }
}