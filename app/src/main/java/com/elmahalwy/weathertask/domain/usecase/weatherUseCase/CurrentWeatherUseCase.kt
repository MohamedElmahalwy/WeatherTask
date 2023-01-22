package com.elmahalwy.weathertask.domain.usecase.weatherUseCase

import android.util.Log
import com.elmahalwy.weathertask.common.Resource
import com.elmahalwy.weathertask.data.dto.currentWeatherResponse.toCurrentWeather
import com.elmahalwy.weathertask.domain.repository.WeatherRepository
import com.elmahalwy.weathertask.domain.model.CurrentWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CurrentWeatherUseCase @Inject constructor(private val repository: WeatherRepository) {
    operator fun invoke(lat: Double, lng: Double): Flow<Resource<CurrentWeather>> = flow {
        try {
            val currentWeather = repository.requestCurrentWeather(lat, lng)
            emit(Resource.Success(currentWeather.toCurrentWeather()))
        } catch (e: HttpException) {
            emit(Resource.DataError(-3))
        } catch (e: IOException) {
            emit(Resource.DataError(-1))
        }
    }

}
