package com.elmahalwy.weathertask.data.dto.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.elmahalwy.weathertask.data.dto.local.WeatherItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherItem(shoppingItem: WeatherItem)


    @Query("SELECT * FROM weather_history_items")
    fun observeAllWeatherHistoryItems(): LiveData<List<WeatherItem>>


}
