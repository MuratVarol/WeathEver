package com.varol.weathever.data.remote.service

import com.varol.weathever.data.remote.model.WeatherResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    companion object {
        const val WEATHER = "weather"
    }

    @GET(WEATHER)
    fun getWeather(
        @Query(value = "lat") lat: Double,
        @Query(value = "lon") lon: Double
    ): Single<WeatherResponseModel>
}