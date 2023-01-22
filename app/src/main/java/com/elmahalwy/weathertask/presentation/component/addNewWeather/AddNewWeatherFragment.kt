package com.elmahalwy.weathertask.presentation.component.addNewWeather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.elmahalwy.weathertask.R
import com.elmahalwy.weathertask.common.Event
import com.elmahalwy.weathertask.common.Resource
import com.elmahalwy.weathertask.data.dto.local.WeatherItem
import com.elmahalwy.weathertask.databinding.FragmentAddNewWeatherBinding
import com.elmahalwy.weathertask.domain.model.CurrentWeather
import com.elmahalwy.weathertask.presentation.base.BaseFragment
import com.elmahalwy.weathertask.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_new_weather.*
import kotlinx.android.synthetic.main.toolbar_tittle_back.*


@AndroidEntryPoint
class AddNewWeatherFragment :
    BaseFragment<AddWeatherViewModel, FragmentAddNewWeatherBinding>(R.layout.fragment_add_new_weather),
    android.location.LocationListener {

    lateinit var weatherImage: Uri
    private var mLocationManager: LocationManager? = null
    private var lat = 0.0
    private var lng = 0.0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvToolbarTittle.text = getString(R.string.addWeather)
        handleClick()
        getCurrentLocation()
        viewModel.getCurrentWeather(lat, lng)
        observeViewModel()
    }


    private fun handleClick() {
        ivBack.setOnClickListener {
            navController.navigateUp()
        }
        btnShare.setOnClickListener {
            HelperUtils.shareTextImage(clContent, requireContext())
        }
    }
    private fun showDataView(show: Boolean) {
        loadingDialog.toGone()
        errorView.visibility = if (show) View.GONE else View.VISIBLE
        clContent.visibility = if (show) View.VISIBLE else View.GONE
        btnShare.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showLoadingView() {
        loadingDialog.toVisible()
        clContent.toGone()
        btnShare.toGone()
    }

    /** Get Current Weather */
    private fun handleWeatherData(response: Resource<CurrentWeather>) {
        when (response) {
            is Resource.Success -> {
                showDataView(true)
                response.data.let { bindData(data = it!!) }
                response.data?.let { insertWeatherItem(it) }
            }

            is Resource.DataError -> {
                showDataView(false)
                response.errorCode?.let { viewModel.showToastMessage(it) }
            }

            is Resource.Loading -> {
                showLoadingView()
            }
        }
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Toast.LENGTH_LONG)
    }
    /** End Get Current Weather */

    /** Insert New Weather */
    private fun handleInsertNewWeather(response: Resource<WeatherItem>) {
        when (response) {
            is Resource.Success -> {
                Snackbar.make(
                    requireActivity().rootLayout,
                    "Added Weather Item",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            is Resource.DataError -> {
                Snackbar.make(
                    requireActivity().rootLayout, "An Error Occured ",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            is Resource.Loading -> {
            }
        }
    }
    /** End Insert New Weather */

    private fun bindData(data: CurrentWeather) {
        weatherImage = arguments?.getParcelable("imageUri")!!
        ivWeather.setImageURI(weatherImage)
        tvCityName.text = data.placeName
        tvWeatherCondition.text = data.weatherCondition
        tvCurrentWeatherTemp.text = data.temp.toString()
        tvTempMin.text = data.tempMin.toString()
        tvTempMax.text = data.tempMax.toString()
        tvWindSpeed.text = data.windSpeed.toString()

    }

    fun insertWeatherItem(currentWeather: CurrentWeather){
        viewModel.insertWeatherItem(
            weatherImage.toString(),
            currentWeather.placeName,
            currentWeather.weatherCondition,
            currentWeather.temp,
            currentWeather.tempMin,
            currentWeather.tempMax,
            currentWeather.windSpeed
        )
    }

    private fun getCurrentLocation() {
        mLocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        try {
            val location: Location = getLastKnownLocation()!!
            if (location != null) {
                lat = location.latitude
                lng = location.longitude
                Log.e("lllat", lat.toString())
                Log.e("llllng", lng.toString())
            } else {
                mLocationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    0f,
                    this
                )
                mLocationManager!!.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    0f,
                    this
                )
            }
        } catch (e: Exception) {

        }

    }

    private fun getLastKnownLocation(): Location? {
        mLocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = mLocationManager!!.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return bestLocation
            }
            val l = mLocationManager!!.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                bestLocation = l
            }
        }
        return bestLocation
    }

    private fun observeViewModel() {
        observe(viewModel.currentWeatherLiveData, ::handleWeatherData)
        observe(viewModel.insertWeatherItemStatus, ::handleInsertNewWeather)
        observeToast(viewModel.showToast)
    }


    override fun getFragmentBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentAddNewWeatherBinding =
        FragmentAddNewWeatherBinding.inflate(inflater, container, false)

    override val viewModel: AddWeatherViewModel by viewModels()

    override fun onLocationChanged(location: Location) {
        lat = location.latitude
        lng = location.longitude
    }
}
