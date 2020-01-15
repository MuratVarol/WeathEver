package com.varol.weathever.data.local.database

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.varol.weathever.data.entity.CoordinationEntity
import com.varol.weathever.data.entity.WeatherTypes
import com.varol.weathever.data.local.database.MoshiBuilder.moshi

class RoomTypeConverter {

    @TypeConverter
    fun fromWeatherType(data: WeatherTypes): String {
        return moshi.adapter(WeatherTypes::class.java).toJson(data)
    }

    @TypeConverter
    fun toWeatherType(json: String): WeatherTypes? {
        return moshi.adapter(WeatherTypes::class.java).fromJson(json)
    }


    @TypeConverter
    fun fromCoordinationEntity(data: CoordinationEntity): String {
        return moshi.adapter(CoordinationEntity::class.java).toJson(data)
    }

    @TypeConverter
    fun toCoordinationEntity(json: String): CoordinationEntity? {
        return moshi.adapter(CoordinationEntity::class.java).fromJson(json)
    }
}

object MoshiBuilder {
    val moshi: Moshi = Moshi.Builder().build()
}