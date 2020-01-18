package com.varol.weathever.domain

import com.varol.weathever.data.entity.WeatherListItemViewEntity
import com.varol.weathever.data.local.database.model.WeatherDo
import com.varol.weathever.data.repository.WeatherRepository
import io.reactivex.Flowable
import javax.inject.Inject

class GetSavedWeatherListUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    fun getSavedWeathers(
    ): Flowable<List<WeatherListItemViewEntity>> {
        return weatherRepository.getSavedWeathersList().map {
            it.map { weatherDo ->
                weatherDo.mapToViewEntity()
            }
        }
    }

    private fun WeatherDo.mapToViewEntity(): WeatherListItemViewEntity {
        return WeatherListItemViewEntity(
            cityId,
            cityName,
            tempInCelsius,
            fetchTime
        )
    }
}