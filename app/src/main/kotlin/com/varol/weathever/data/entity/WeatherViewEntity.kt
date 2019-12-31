package com.varol.weathever.data.entity

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.varol.weathever.R
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class WeatherViewEntity(
    val cityId: Long,
    val cityName: String,
    val tempInCelsius: String,
    val realFeel: String,
    val tempMax: String,
    val tempMin: String,
    val humidity: String,
    val weatherTypes: WeatherTypes,
    val coordinates: CoordinationEntity,
    val cloudRate: Int,
    val windSpeed: Double,
    val country: String,
    val sunrise: Date,
    val sunset: Date
) : Parcelable

@Parcelize
data class WeatherTypes(
    @DrawableRes
    val weatherIconDrawable: Int,
    val description: String
) : Parcelable

@Parcelize
data class CoordinationEntity(
    val lat: Double,
    val lon: Double
) : Parcelable

fun weatherIconSelector(iconId: Int): Int {
    return when (iconId) {

        in 200..232 -> R.drawable.ic_weather_storm

        in 300..321, in 520..531 -> R.drawable.ic_weather_dark_rain

        in 300..321 -> R.drawable.ic_weather_dark_rain

        in 500..504 -> R.drawable.ic_weather_sunny_rain

        in 600..622, 511 -> R.drawable.ic_weather_snowy

        in 701..781 -> R.drawable.ic_weather_haze

        800 -> R.drawable.ic_weather_sunny

        else -> R.drawable.ic_weather_all
    }
}