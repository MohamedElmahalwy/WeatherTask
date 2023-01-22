package com.elmahalwy.weathertask.utils.CamerUtils


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elmahalwy.weathertask.databinding.FragmentCameraGalleryBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CameraGalleryBottomSheet(var clickListener: CameraGalleryInterface) :
    BottomSheetDialogFragment() {

    var changetext = false
    private var title: String? = null
    lateinit var binding: FragmentCameraGalleryBottomsheetBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCameraGalleryBottomsheetBinding.inflate(inflater, container, false)
        clicks()
        return binding.root
    }

    private fun clicks() {
        if (changetext) {
            binding.tvBottomSheetTitle.setText(title)
        }
        binding.liCamera.setOnClickListener { clickListener.OnCameraClick() }

        binding.liGallery.setOnClickListener { clickListener.OnGalleryClick() }
    }


    fun setTitle(title: String?) {
        this.title = title
        changetext = true
    }

    interface CameraGalleryInterface {
        fun OnCameraClick()
        fun OnGalleryClick()
    }


}
