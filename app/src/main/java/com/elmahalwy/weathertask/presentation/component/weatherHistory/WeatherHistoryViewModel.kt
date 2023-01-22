package com.elmahalwy.weathertask.presentation.component.weatherHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elmahalwy.weathertask.data.dto.local.WeatherItem
import com.elmahalwy.weathertask.domain.repository.WeatherRepository
import com.elmahalwy.weathertask.domain.usecase.weatherHistoryUseCase.WeatherHistoryUseCase
import com.elmahalwy.weathertask.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherHistoryViewModel @Inject constructor(
    private val weatherHistoryUseCase: WeatherHistoryUseCase
) : BaseViewModel() {

    lateinit var weatherHistoryLiveData : LiveData<List<WeatherItem>>

    fun getWeatherHistory() {
        viewModelScope.launch {
            weatherHistoryUseCase.invoke().collect{
                weatherHistoryLiveData = it
            }
        }
    }
}

