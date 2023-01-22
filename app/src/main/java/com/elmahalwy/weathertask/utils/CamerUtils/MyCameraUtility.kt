package com.elmahalwy.weathertask.utils.CamerUtils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import com.elmahalwy.weathertask.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MyCameraUtility {

    companion object {
        private const val TAG = "MyCameraUtility"
        var currentPhotoPath: String? = null
        var cameraGalleryBottomSheet: CameraGalleryBottomSheet? = null
        const val GALLERY_PERMISSION_REQUEST = 9000
        const val CAMERA_PERMISSION_REQUEST = 8000
        const val CAMERA_REQUEST = 900
        const val GALLERY_REQUEST = 700

        private const val READ_EXTERNAL_STORAGE =
            Manifest.permission.READ_EXTERNAL_STORAGE
        private const val WRITE_EXTERNAL_STORAGE =
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA


        fun showImageOptionsBottomSheet(
            activity: Activity?,
            fragmentManager: FragmentManager?
        ) {
            val bottomSheetClickListener: CameraGalleryBottomSheet.CameraGalleryInterface =
                object : CameraGalleryBottomSheet.CameraGalleryInterface {
                    override fun OnCameraClick() {
                        if (hasCameraPermission(
                                activity, CAMERA_PERMISSION_REQUEST
                            )
                        ) {
                            takePhoto(activity!!)
                        }
                        cameraGalleryBottomSheet?.dismiss()
                    }

                    override fun OnGalleryClick() {
                        if (hasCameraPermission(
                                activity,
                                GALLERY_PERMISSION_REQUEST
                            )
                        ) {
                            chooseImageFromGallary(activity!!)
                        }
                        cameraGalleryBottomSheet?.dismiss()
                    }
                }

            cameraGalleryBottomSheet =
                CameraGalleryBottomSheet(bottomSheetClickListener)
            cameraGalleryBottomSheet!!.setStyle(
                BottomSheetDialogFragment.STYLE_NORMAL,
                R.style.AppBottomSheetDialogTheme
            )

            cameraGalleryBottomSheet?.show(
                fragmentManager!!,
                "cameraGalleryBottomSheet"
            )
            cameraGalleryBottomSheet?.setTitle(activity!!.getString(R.string.choose_method_to_upload_image))
        }


        fun hasCameraPermission(activity: Activity?, permissionRequest: Int): Boolean {
            Log.v(TAG, "Checking For Permissions")
            val permissions = arrayOf(
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE,
                CAMERA_PERMISSION
            )
            return if (ContextCompat.checkSelfPermission(
                    activity!!,
                    READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    activity,
                    WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    activity,
                    CAMERA_PERMISSION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permissions Found")
                true
            } else {
                Log.v(TAG, "Not Found and asking for them")
                ActivityCompat.requestPermissions(activity, permissions, permissionRequest)
                false
            }
        }

        @SuppressLint("QueryPermissionsNeeded")
        fun takePhoto(activity: Activity) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
                // Create the File where the photo should go
                var photoFile: File? = null
                try {
                    photoFile = createImageFile(activity)
                } catch (ex: IOException) {
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(
                        activity,
                        "com.elmahalwy.weathertask.fileprovider",
                        photoFile
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    activity.startActivityForResult(
                        takePictureIntent,
                        CAMERA_REQUEST
                    )
                }
            }
        }

        @Throws(IOException::class)
        fun createImageFile(context: Context): File? {
            // Create an image file name
            val timeStamp =
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
            )

            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = image.absolutePath
            return image
        }


        fun chooseImageFromGallary(activity: Activity) {
            Log.e("requestCode", GALLERY_REQUEST.toString() + "ksks")
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activity.startActivityForResult(
                Intent.createChooser(
                    intent,
                    activity.resources.getString(R.string.choose_photo)
                ), GALLERY_REQUEST
            )
        }

        @Throws(IOException::class)
        fun fixRotate(photoPath: String?, bitmap: Bitmap): Bitmap? {
            var bitmap = bitmap
            val ei = ExifInterface(photoPath!!)
            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    Log.v("kkkkk", "90")
                    bitmap = rotateImage(90f, bitmap)
                }
                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    Log.v("kkkkk", "180")
                    bitmap = rotateImage(180f, bitmap)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    Log.v("kkkkk", "270")
                    bitmap = rotateImage(270f, bitmap)
                }
                ExifInterface.ORIENTATION_NORMAL -> Log.v("kkkkk", "360")
                else -> {
                }
            }
            return bitmap
        }

        fun rotateImage(angle: Float, bitmap: Bitmap): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        }

    }


}
