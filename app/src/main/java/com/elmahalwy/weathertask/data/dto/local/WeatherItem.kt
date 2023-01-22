package com.elmahalwy.weathertask.data.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_history_items")
data class WeatherItem(
    val placeName: String,
    val weatherCondition: String,
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val windSpeed: Double,
    val image: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
