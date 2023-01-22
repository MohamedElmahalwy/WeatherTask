package com.elmahalwy.weathertask.presentation.component.weatherHistory

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmahalwy.weathertask.R
import com.elmahalwy.weathertask.databinding.FragmentWeatherHistoryBinding
import com.elmahalwy.weathertask.presentation.base.BaseFragment
import com.elmahalwy.weathertask.utils.CamerUtils.MyCameraUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_weather_history.*
import kotlinx.android.synthetic.main.toolbar_tittle.*
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class WeatherHistoryFragment :
    BaseFragment<WeatherHistoryViewModel, FragmentWeatherHistoryBinding>(R.layout.fragment_weather_history) {

    @set:Inject
    var weatherHistoryItemAdapter: WeatherHistoryItemAdapter? = null

    private var mStorageGranted = false
    private lateinit var selectedImage: Uri
    private val REQUEST_CODE_LOCATIOn = 23


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvToolbarTittle.text = getString(R.string.history)
        handleClick()
        setupRecyclerView()
        getWeatherHistory()
    }

    fun handleClick() {
        tvAddWeather.setOnClickListener {
            requestPermission()
        }
    }

    private fun setupRecyclerView() {
        rvWeatherHistory.apply {
            adapter = weatherHistoryItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getWeatherHistory() {
        viewModel.getWeatherHistory()
        viewModel.weatherHistoryLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                showDataView(true)
            } else {
                showDataView(false)
                weatherHistoryItemAdapter?.weatherHistoryItems = it
            }
        })
    }


    private fun showDataView(show: Boolean) {
        rvWeatherHistory.visibility = if (show) View.GONE else View.VISIBLE
        emptyView.visibility = if (show) View.VISIBLE else View.GONE
    }

    /** Navigate to add weather */
    fun navagiteToAddWeather() {
        val extra = Bundle()
        extra.putParcelable("imageUri", selectedImage)
        navController.safeNavigate(
            R.id.weatherHistoryFragment,
            R.id.action_weatherHistoryFragment_to_addNewWeatherFragment, extra
        )
    }
    /** End Navigate to add weather */


    /** Request Location Permission */
    private fun requestPermission() {
        val hasForegroundPermission = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (hasForegroundPermission) {
            MyCameraUtility.showImageOptionsBottomSheet(requireActivity(), childFragmentManager)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ), REQUEST_CODE_LOCATIOn
            )
        }
    }
    /** End Request Location Permission */


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mStorageGranted = false
        when (requestCode) {
            MyCameraUtility.GALLERY_PERMISSION_REQUEST -> if (grantResults.isNotEmpty()) {
                var i = 0
                while (i < grantResults.size) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        mStorageGranted = false
                        return
                    }
                    i++
                }
                mStorageGranted = true
                MyCameraUtility.chooseImageFromGallary(requireActivity())
            } else {
                mStorageGranted = false
            }
            MyCameraUtility.CAMERA_PERMISSION_REQUEST -> if (grantResults.isNotEmpty()) {
                var i = 0
                while (i < grantResults.size) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        mStorageGranted = false
                        return
                    }
                    i++
                }
                mStorageGranted = true
                MyCameraUtility.takePhoto(requireActivity())
            } else {
                mStorageGranted = false
            }
            REQUEST_CODE_LOCATIOn -> {
                MyCameraUtility.showImageOptionsBottomSheet(requireActivity(), childFragmentManager)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MyCameraUtility.GALLERY_REQUEST) {
            if (data != null && data.data != null) {
                selectedImage = data.data!!
                navagiteToAddWeather()
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == MyCameraUtility.CAMERA_REQUEST) {
            if (!MyCameraUtility.currentPhotoPath.equals("")) {
                selectedImage = Uri.fromFile(File(MyCameraUtility.currentPhotoPath))
                navagiteToAddWeather()
            } else {
                Toast.makeText(context, "empty path", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWeatherHistoryBinding =
        FragmentWeatherHistoryBinding.inflate(inflater, container, false)

    override val viewModel: WeatherHistoryViewModel by viewModels()


}
