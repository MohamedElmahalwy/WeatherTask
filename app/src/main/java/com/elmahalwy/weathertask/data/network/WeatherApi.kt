package com.elmahalwy.weathertask.data.network

import com.elmahalwy.weathertask.common.APP_ID
import com.elmahalwy.weathertask.data.dto.currentWeatherResponse.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String = APP_ID,
    ): CurrentWeatherResponse
}
