package com.elmahalwy.weathertask.presentation.component.addNewWeather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elmahalwy.weathertask.common.Resource
import com.elmahalwy.weathertask.data.dto.local.WeatherItem
import com.elmahalwy.weathertask.domain.model.CurrentWeather
import com.elmahalwy.weathertask.domain.usecase.weatherHistoryUseCase.AddWeatherItemUseCase
import com.elmahalwy.weathertask.domain.usecase.weatherUseCase.CurrentWeatherUseCase
import com.elmahalwy.weathertask.util.MainCoroutineRule
import com.elmahalwy.weathertask.util.TestCoroutineRule
import com.elmahalwy.weathertask.util.getOrAwaitValueTest
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustAwait
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddWeatherViewModelTest {
    // Subject under test
    private lateinit var addWeatherViewModel: AddWeatherViewModel


    // Use a fake UseCase to be injected into the viewModel
    private var currentWeatherUseCase: CurrentWeatherUseCase = mockk()
    private var addWeatherHistoryUseCase: AddWeatherItemUseCase = mockk()


    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        addWeatherViewModel = AddWeatherViewModel(currentWeatherUseCase, addWeatherHistoryUseCase)
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `insert weather item with all fields, return True `() = runTest {
        val weatherItem = WeatherItem(
            "Place Name", "Clear", 237.3,
            100.2, 300.2, 21.2, "image"
        )

        coJustAwait { addWeatherViewModel.insertWeatherItemIntoDb(weatherItem) }
        addWeatherViewModel.insertWeatherItemStatus.observeForever { }

        assertThat(weatherItem).isEqualTo(
            addWeatherViewModel.insertWeatherItemStatus.getOrAwaitValueTest().data
        )

    }


    @Test
    fun `get Current Weather() then invoke requestGetCurrentWeather`() = runTest {
        // Let's do an answer for the liveData
        val currentWeather =
            CurrentWeather("Weather Condition", 237.3, 100.2, 300.1, "Place Name", 21.2)

        //1- Mock calls
        coEvery { currentWeatherUseCase.invoke(36.3, 36.3) } returns flow {
            emit(Resource.Success(currentWeather))
        }

        //2-Call
        addWeatherViewModel.getCurrentWeather(36.3, 36.3)
        //active observer for livedata
        addWeatherViewModel.currentWeatherLiveData.observeForever { }

        //3-verify
        val dataValue =
            addWeatherViewModel.currentWeatherLiveData.getOrAwaitValueTest().data
        assertThat(currentWeather).isEqualTo(dataValue)

    }


}
