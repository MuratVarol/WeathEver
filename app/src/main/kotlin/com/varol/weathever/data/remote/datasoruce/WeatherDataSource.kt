package com.varol.weathever.data.remote.datasoruce

import com.varol.weathever.data.remote.Either
import com.varol.weathever.data.remote.ServiceRequestHandler
import com.varol.weathever.data.remote.model.WeatherResponseModel
import com.varol.weathever.data.remote.service.WeatherService
import com.varol.weathever.internal.util.Failure
import io.reactivex.Single
import javax.inject.Inject

typealias service = ServiceRequestHandler

class WeatherDataSource @Inject constructor(private val weatherService: WeatherService) {

    fun getLocationWeather(
        lat: Double,
        lon: Double
    ): Single<Either<Failure, WeatherResponseModel>> {
        return service.sendRequest(weatherService.getCurrentLocationWeather(lat, lon))
    }

    fun getWeatherByCityId(
        cityId: Long
    ): Single<Either<Failure, WeatherResponseModel>> {
        return service.sendRequest(weatherService.getWeatherByCityId(cityId))
    }

    fun getWeatherByCityName(
        cityName: String
    ): Single<Either<Failure, WeatherResponseModel>> {
        return service.sendRequest(weatherService.getWeatherByCityName(cityName))
    }
}
