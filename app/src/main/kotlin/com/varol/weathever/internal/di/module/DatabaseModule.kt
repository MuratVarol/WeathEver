package com.varol.weathever.internal.di.module

import android.content.Context
import androidx.room.Room
import com.varol.weathever.data.local.database.AppDatabase
import com.varol.weathever.data.local.database.dao.WeatherDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

const val APP_DATABASE_NAME = "weather_db"

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun createAppDatabase(context: Context): AppDatabase {
        return Room
            .databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                APP_DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun createWeatherDao(appDatabase: AppDatabase): WeatherDao {
        return appDatabase.weathersDao()
    }
}