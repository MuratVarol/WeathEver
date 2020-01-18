package com.varol.weathever.data.local.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.varol.weathever.data.entity.CoordinationEntity
import com.varol.weathever.data.entity.WeatherTypes
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "tb_weathers")
data class WeatherDo(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val cityId: Long,
    @ColumnInfo(name = "city")
    val cityName: String,
    @ColumnInfo(name = "temp")
    val tempInCelsius: String,
    @ColumnInfo(name = "reel_feel")
    val realFeel: String,
    @ColumnInfo(name = "temp_max")
    val tempMax: String,
    @ColumnInfo(name = "temp_min")
    val tempMin: String,
    @ColumnInfo(name = "humidity")
    val humidity: String,
    @ColumnInfo(name = "weather_type")
    val weatherTypes: WeatherTypes,
    @ColumnInfo(name = "coordinates")
    val coordinates: CoordinationEntity,
    @ColumnInfo(name = "cloud_rate")
    val cloudRate: String,
    @ColumnInfo(name = "wind_speed")
    val windSpeed: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "sunrise")
    val sunrise: Long,
    @ColumnInfo(name = "sunset")
    val sunset: Long,
    @ColumnInfo(name = "fetch_time")
    val fetchTime: Long

) : Parcelable