package com.elmahalwy.weathertask.domain.repository

import androidx.lifecycle.LiveData
import com.elmahalwy.weathertask.data.dto.currentWeatherResponse.CurrentWeatherResponse
import com.elmahalwy.weathertask.data.dto.local.WeatherItem

interface WeatherRepository {
    suspend fun requestCurrentWeather(lat: Double, lng: Double): CurrentWeatherResponse

    suspend fun insertWeatherHistoryItem(weatherHistoryItem: WeatherItem)

    fun observeAllWeatherHistoryItems(): LiveData<List<WeatherItem>>

}
