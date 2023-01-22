package com.elmahalwy.weathertask.data.repository

import androidx.lifecycle.LiveData
import com.elmahalwy.weathertask.data.dto.currentWeatherResponse.CurrentWeatherResponse
import com.elmahalwy.weathertask.data.dto.local.WeatherHistoryDao
import com.elmahalwy.weathertask.data.dto.local.WeatherItem
import com.elmahalwy.weathertask.data.network.WeatherApi
import com.elmahalwy.weathertask.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherHistoryDao: WeatherHistoryDao, val api: WeatherApi
) : WeatherRepository {

    override suspend fun requestCurrentWeather(lat: Double, lng: Double): CurrentWeatherResponse {
        return api.getCurrentWeather(lat, lng)
    }

    override suspend fun insertWeatherHistoryItem(weatherHistoryItem: WeatherItem) {
        weatherHistoryDao.insertWeatherItem(weatherHistoryItem)
    }

    override fun observeAllWeatherHistoryItems(): LiveData<List<WeatherItem>> {
        return weatherHistoryDao.observeAllWeatherHistoryItems()

    }

}
