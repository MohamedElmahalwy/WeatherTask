package com.elmahalwy.weathertask.domain.model

data class CurrentWeather(
    val weatherCondition: String,
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val placeName: String,
    val windSpeed: Double,

)
