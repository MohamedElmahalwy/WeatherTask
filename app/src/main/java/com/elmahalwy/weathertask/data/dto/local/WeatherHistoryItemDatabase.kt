package com.elmahalwy.weathertask.data.dto.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WeatherItem::class],
    version = 1
)
abstract class WeatherHistoryItemDatabase : RoomDatabase() {
    abstract fun weatherHistoryDao(): WeatherHistoryDao
}
