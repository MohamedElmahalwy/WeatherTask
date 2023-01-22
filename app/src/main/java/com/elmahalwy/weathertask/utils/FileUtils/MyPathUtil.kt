package com.skyline.baqla.utils.FileUtils

import android.content.Context
import android.net.Uri
import java.io.*


object MyPathUtil {
    fun getImagePathFromInputStreamUri(
        context: Context,
        uri: Uri
    ): String? {
        var inputStream: InputStream? = null
        var filePath: String? = null
        if (uri.authority != null) {
            try {
                inputStream = context.contentResolver.openInputStream(uri) // context needed
                val photoFile =
                    createTemporalFileFrom(
                        context,
                        inputStream
                    )
                filePath = photoFile!!.path
            } catch (e: FileNotFoundException) {
                // log
            } catch (e: IOException) {
                // log
            } finally {
                try {
                    inputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return filePath
    }

    @Throws(IOException::class)
    private fun createTemporalFileFrom(
        context: Context,
        inputStream: InputStream?
    ): File? {
        var targetFile: File? = null
        if (inputStream != null) {
            var read: Int
            val buffer = ByteArray(8 * 1024)
            targetFile =
                createTemporalFile(
                    context
                )
            val outputStream: OutputStream = FileOutputStream(targetFile)
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return targetFile
    }

    private fun createTemporalFile(context: Context): File {
        return File(context.externalCacheDir, "tempFile.pdf") // context needed
    }
}
