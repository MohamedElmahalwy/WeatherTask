package com.elmahalwy.weathertask.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import java.io.ByteArrayOutputStream

object HelperUtils {
    fun getScreenShot(view: View): Bitmap? {
        val screenView = view
        screenView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(screenView.drawingCache)
        screenView.isDrawingCacheEnabled = false
        return bitmap
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "weather Photo",
                null
            )
        return Uri.parse(path)
    }


    fun shareTextImage(view: View, context: Context) {
        val bitmap = getScreenShot(view)!!
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(context, bitmap))
        shareIntent.type = "image/png"
        context.startActivity(Intent.createChooser(shareIntent, "Share with"))
    }


}


