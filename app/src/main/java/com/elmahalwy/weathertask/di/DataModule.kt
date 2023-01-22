package com.elmahalwy.weathertask.di

import com.elmahalwy.weathertask.data.repository.WeatherRepositoryImpl
import com.elmahalwy.weathertask.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Tells Dagger this is a Dagger module
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun provideDataRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository
}
