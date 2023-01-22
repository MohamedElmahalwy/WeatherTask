package com.elmahalwy.weathertask.domain.usecase.weatherHistoryUseCase

import com.bumptech.glide.load.HttpException
import com.elmahalwy.weathertask.common.Resource
import com.elmahalwy.weathertask.data.dto.local.WeatherItem
import com.elmahalwy.weathertask.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class AddWeatherItemUseCase @Inject constructor(private val repository: WeatherRepository) {
   suspend  operator fun invoke(weatherItem: WeatherItem) = flow {
        try {
            val weatherInnerItem = repository.insertWeatherHistoryItem(weatherItem)
            emit(Resource.Success(weatherInnerItem))
        } catch (e: HttpException) {
            emit(Resource.DataError(-3))
        } catch (e: IOException) {
            emit(Resource.DataError(-1))
        }
    }


}
