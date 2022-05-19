package com.dev.retrofit_in_use.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.log10
import kotlin.math.pow

@HiltViewModel
class CompareViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    var imageUri: Uri? = null

    // Get Image File
    fun getImageSizeFromUri(context: Context?, imageUri: Uri?): String {
        val cursor = imageUri?.let {
            context?.contentResolver?.query(
                it,
                null,
                null,
                null,
                null
            )
        }

        return if (cursor != null && cursor.moveToFirst()) {
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (!cursor.isNull(sizeIndex)) {
                decodeSize(cursor.getLong(sizeIndex))
            } else {
                "0"
            }
        } else {
            "0"
        }
    }

    // Calculate Size From Byte
    private fun decodeSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }

        val units = arrayOf("B", "KB", "MB", "GB", "TB")

        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()

        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }

}