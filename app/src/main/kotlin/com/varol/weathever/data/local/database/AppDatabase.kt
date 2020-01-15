package com.varol.weathever.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.varol.weathever.BuildConfig
import com.varol.weathever.data.local.database.dao.WeatherDao
import com.varol.weathever.data.local.database.model.WeatherDo

@Database(
    entities = [WeatherDo::class], version = BuildConfig.VERSION_CODE, exportSchema = false
)
@TypeConverters(RoomTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weathersDao(): WeatherDao
}