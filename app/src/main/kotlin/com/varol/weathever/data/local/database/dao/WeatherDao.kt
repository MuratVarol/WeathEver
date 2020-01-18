package com.varol.weathever.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.varol.weathever.data.local.database.model.WeatherDo
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface WeatherDao : BaseDao<WeatherDo> {
    @Query("SELECT * FROM tb_weathers")
    fun getAllSavedWeathers(): Flowable<List<WeatherDo>>

    @Query("SELECT * FROM tb_weathers WHERE city = :city")
    fun getWeatherByName(city: String): Single<WeatherDo?>

    @Query("SELECT * FROM tb_weathers WHERE id = :cityId")
    fun getWeatherByCityId(cityId: Long): Single<WeatherDo?>

    @Query("DELETE FROM tb_weathers WHERE id = :cityId")
    fun deleteWeatherByCityId(cityId: Long): Single<Int>
}