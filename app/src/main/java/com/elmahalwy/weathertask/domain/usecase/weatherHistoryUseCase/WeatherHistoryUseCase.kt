package com.elmahalwy.weathertask.domain.usecase.weatherHistoryUseCase

import androidx.lifecycle.LiveData
import com.elmahalwy.weathertask.data.dto.local.WeatherItem
import com.elmahalwy.weathertask.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherHistoryUseCase @Inject constructor(private val repository: WeatherRepository) {
    operator fun invoke(): Flow<LiveData<List<WeatherItem>>> = flow {
        val historyWeather = repository.observeAllWeatherHistoryItems()
        emit(historyWeather)
    }

}
