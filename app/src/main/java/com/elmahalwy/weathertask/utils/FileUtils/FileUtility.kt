package com.skyline.baqla.utils.FileUtils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import com.elmahalwy.weathertask.utils.CamerUtils.MyCameraUtility
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream



var currentPhotoPath: String? = null

fun getMultiPart(
    context: Context,
    uri: Uri,
    variableName: String?,
    name: String
): MultipartBody.Part {
    var mBitmap: Bitmap? = null
    try {
//            mBitmap= BitmapFactory.decodeFile(uri.getPath(), new BitmapFactory.Options());
        mBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        mBitmap = MyCameraUtility.fixRotate(uri.encodedPath, mBitmap)
    } catch (e: IOException) {
        Log.v("kkkkk", "bitmap exception: " + e.message)
    }
    val filesDir = context.applicationContext.filesDir
    val file = File(filesDir, "$name.jpg")
    val os: OutputStream
    try {
        os = FileOutputStream(file)
        Log.i("sadsad", mBitmap!!.byteCount.toString() + "")
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 30, os)
        os.flush()
        os.close()
    } catch (e: Exception) {
        Log.e("Error writing bitmap", e.message!!)
    }
    val requestFile =
        RequestBody.create("image/png".toMediaTypeOrNull(), file)
    return MultipartBody.Part.createFormData(variableName.toString(), file.name, requestFile)
}

fun getFileMultiPart(
    context: Context?,
    uri: Uri?,
    variableName: String?
): MultipartBody.Part? {
    val file =
        File(
            MyPathUtil.getImagePathFromInputStreamUri(
                context!!,
                uri!!
            )
        )
    return if (file.exists() && file.canRead()) {
        val requestFile: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        MultipartBody.Part.createFormData(variableName.toString(), file.name, requestFile)
    } else {
        null
    }
}

fun getFileMultiPart(
    uri: Uri?,
    filePath: String?,
    variableName: String?
): MultipartBody.Part? {
    val file = File(filePath)
    return if (file.exists() && file.canRead()) {
        val requestFile =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        MultipartBody.Part.createFormData(variableName.toString(), file.name, requestFile)
    } else {
        null
    }
}

fun getFileNameByUri(resolver: ContentResolver, uri: Uri?): String {
    val returnCursor = resolver.query(uri!!, null, null, null, null)!!
    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    returnCursor.close()
    return name
}


fun toRequestBody(value: String?): RequestBody? {
    val body: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), value!!)

    return body
}
