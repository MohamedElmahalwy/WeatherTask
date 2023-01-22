package com.elmahalwy.weathertask.di

import android.content.Context
import androidx.room.Room
import com.elmahalwy.weathertask.common.DATABASE_NAME
import com.elmahalwy.weathertask.data.dto.local.WeatherHistoryItemDatabase
import com.elmahalwy.weathertask.data.network.ServiceGenerator
import com.elmahalwy.weathertask.data.network.WeatherApi
import com.elmahalwy.weathertask.utils.Network
import com.elmahalwy.weathertask.utils.NetworkConnectivity


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideServiceGenetator(): WeatherApi {
        return ServiceGenerator().returnRetrofitInstance()
    }

    @Provides
    @Singleton
    fun provideServiceGenrator(@ApplicationContext context: Context): NetworkConnectivity {
        return Network(context)
    }

    @Singleton
    @Provides
    fun provideWeatherItemDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, WeatherHistoryItemDatabase::class.java, DATABASE_NAME).build()


    @Singleton
    @Provides
    fun provideWeatherHistoryDao(
        database: WeatherHistoryItemDatabase
    ) = database.weatherHistoryDao()


}
