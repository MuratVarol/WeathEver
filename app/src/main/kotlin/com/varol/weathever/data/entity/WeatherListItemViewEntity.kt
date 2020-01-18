package com.varol.weathever.data.entity

data class WeatherListItemViewEntity(
    val id: Long,
    val city: String,
    val degreeInCelsius: String,
    val fetchDate: Long
)


