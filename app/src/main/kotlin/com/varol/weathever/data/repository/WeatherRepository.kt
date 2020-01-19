package com.varol.weathever.data.repository

import com.varol.weathever.data.entity.CoordinationEntity
import com.varol.weathever.data.entity.WeatherTypes
import com.varol.weathever.data.entity.WeatherViewEntity
import com.varol.weathever.data.entity.weatherIconSelector
import com.varol.weathever.data.local.database.dao.WeatherDao
import com.varol.weathever.data.local.database.model.WeatherDo
import com.varol.weathever.data.remote.Either
import com.varol.weathever.data.remote.datasoruce.WeatherDataSource
import com.varol.weathever.data.remote.model.WeatherResponseModel
import com.varol.weathever.internal.extension.appendMeterPerSecond
import com.varol.weathever.internal.extension.appendPercentage
import com.varol.weathever.internal.extension.toDate
import com.varol.weathever.internal.extension.toTimeStamp
import com.varol.weathever.internal.util.Failure
import io.reactivex.Flowable
import io.reactivex.Single
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherDataSource: WeatherDataSource,
    private val weatherDao: WeatherDao
) {
    fun getWeatherByLocation(lat: Double, lon: Double): Single<Either<Failure, WeatherViewEntity>> {
        val result = weatherDataSource.getLocationWeather(lat, lon)
        return result.map { weather ->
            weather.transform { it.mapToViewEntity() }
        }
    }

    fun getWeatherByCityIdRemote(cityId: Long): Single<Either<Failure, WeatherViewEntity>> {
        val result = weatherDataSource.getWeatherByCityId(cityId)
        return result.map { weather ->
            weather.transform { it.mapToViewEntity() }
        }
    }

    fun getWeatherByCityNameRemote(cityName: String): Single<Either<Failure, WeatherViewEntity>> {
        val result = weatherDataSource.getWeatherByCityName(cityName)
        return result.map { weather ->
            weather.transform { it.mapToViewEntity() }
        }
    }

    fun getWeatherByCityIdLocal(cityId: Long): Single<WeatherViewEntity> {
        val result = weatherDao.getWeatherByCityId(cityId)
        return result.map { weather ->
            weather.toViewEntity()
        }
    }

    fun getSavedWeathersList(): Flowable<List<WeatherDo>> {
        return weatherDao.getAllSavedWeathers()
    }

    fun saveCurrentWeatherToDb(weather: WeatherViewEntity): Single<Long> {
        return weatherDao.insert(weather.toDoObject())
    }

    fun deleteWeatherFromDbByCityId(cityId: Long): Single<Int> {
        return weatherDao.deleteWeatherByCityId(cityId)
    }

    fun deleteWeatherFromDb(weather: WeatherViewEntity): Single<Int> {
        return weatherDao.delete(weather.toDoObject())
    }

    fun updateWeatherOnDb(weather: WeatherViewEntity): Single<Int> {
        return weatherDao.update(weather.toDoObject())
    }

    fun getWeatherByCityNameLocal(cityName: String): Single<WeatherDo?> {
        return weatherDao.getWeatherByName(cityName)
    }

    private fun WeatherResponseModel.mapToViewEntity(): WeatherViewEntity {
        return WeatherViewEntity(
            cityId,
            cityName,
            main.temp.toString(),
            main.realFeel.toString(),
            main.tempMax.toString(),
            main.tempMin.toString(),
            main.humidity.toString(),
            WeatherTypes(
                weatherIconSelector(weather.first().id),
                weather.first().description
            ),
            CoordinationEntity(coord.lat, coord.lon),
            clouds.all.toString(),
            wind.speed.toString(),
            sys.country,
            sys.sunrise.toDate(),
            sys.sunset.toDate(),
            Calendar.getInstance().timeInMillis
        )
    }

    private fun WeatherViewEntity.toDoObject(): WeatherDo {
        return WeatherDo(
            cityId,
            cityName,
            tempInCelsius,
            realFeel,
            tempMax,
            tempMin,
            humidity,
            weatherTypes,
            coordinates,
            cloudRate,
            windSpeed,
            country,
            sunrise.toTimeStamp(),
            sunset.toTimeStamp(),
            fetchTime
        )
    }

    private fun WeatherDo.toViewEntity(): WeatherViewEntity {
        return WeatherViewEntity(
            cityId,
            cityName,
            tempInCelsius,
            realFeel,
            tempMax,
            tempMin,
            humidity,
            weatherTypes,
            coordinates,
            cloudRate,
            windSpeed,
            country,
            sunrise.toDate(),
            sunset.toDate(),
            fetchTime
        )
    }

}