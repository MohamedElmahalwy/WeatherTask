package com.elmahalwy.weathertask.presentation.component.addNewWeather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elmahalwy.weathertask.common.Resource
import com.elmahalwy.weathertask.data.dto.local.WeatherItem
import com.elmahalwy.weathertask.domain.usecase.weatherHistoryUseCase.AddWeatherItemUseCase
import com.elmahalwy.weathertask.domain.usecase.weatherUseCase.CurrentWeatherUseCase
import com.elmahalwy.weathertask.presentation.base.BaseViewModel
import com.elmahalwy.weathertask.utils.SingleEvent
import com.elmahalwy.weathertask.domain.model.CurrentWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWeatherViewModel @Inject constructor(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val addWeatherHistoryUseCase: AddWeatherItemUseCase,
) : BaseViewModel() {

    private val currentWeatherLiveDataPrivate =
        MutableLiveData<Resource<CurrentWeather>>()
    val currentWeatherLiveData: LiveData<Resource<CurrentWeather>> get() = currentWeatherLiveDataPrivate

    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate

    fun getCurrentWeather(lat: Double, lng: Double) {
        viewModelScope.launch {
            currentWeatherLiveDataPrivate.value = Resource.Loading()
            currentWeatherUseCase(lat, lng).collect {
                currentWeatherLiveDataPrivate.value = it
            }
        }
    }

    private val _insertWeatherItemStatus = MutableLiveData<Resource<WeatherItem>>()
    val insertWeatherItemStatus: LiveData<Resource<WeatherItem>> =
        _insertWeatherItemStatus


    fun insertWeatherItem(
        image: String,
        name: String,
        weatherCondition: String,
        weatherTemp: Double,
        minTemp: Double,
        maxTemp: Double,
        windSpeed: Double,
    ) {
        val weatherItem =
            WeatherItem(name, weatherCondition, weatherTemp, minTemp, maxTemp, windSpeed, image)
        insertWeatherItemIntoDb(weatherItem)
    }

    fun insertWeatherItemIntoDb(weatherItem: WeatherItem) = viewModelScope.launch {
        addWeatherHistoryUseCase(weatherItem).collect{}
        _insertWeatherItemStatus.postValue(Resource.Success(weatherItem))
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }


}
