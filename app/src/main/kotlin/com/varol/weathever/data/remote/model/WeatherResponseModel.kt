package com.varol.weathever.data.remote.model

import com.squareup.moshi.Json
import com.varol.weathever.data.entity.WeatherTypes

data class WeatherResponseModel(
    @Json(name = "id")
    val cityId: Long,
    @Json(name = "name")
    val cityName: String,
    @Json(name = "timezone")
    val timeZone: String,
    val coord: CoordinationEntity,
    val weather: List<WeatherItemEntity>,
    val main: MainWeatherEntity,
    val clouds: CloudsEntity,
    val wind: WindEntity,
    val sys: SysEntity

) : BaseResponseModel(cityId)

data class CoordinationEntity(
    val lat: Double,
    val lon: Double
)

data class WeatherItemEntity(
    val id: Int,
    val main: WeatherTypes,
    val description: String
)

data class MainWeatherEntity(
    val temp: Double,
    @Json(name = "feels_like")
    val realFeel: Double,
    @Json(name = "temp_max")
    val tempMax: Double,
    @Json(name = "temp_min")
    val tempMin: Double,
    @Json(name = "humidity")
    val humidity: Int
)

data class WindEntity(
    val speed: Double,
    val deg: Int
)

data class CloudsEntity(
    val all: Int
)

data class SysEntity(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)