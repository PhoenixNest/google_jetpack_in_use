package com.dev.retrofit_in_use.ui.binding_adapters

import android.net.Uri
import android.widget.Button
import androidx.databinding.BindingAdapter

class CompareBinding {

    companion object {
        @BindingAdapter("android:compressImage")
        @JvmStatic
        fun compressImage(button: Button, imageUri: Uri) {
            button.setOnClickListener {

            }
        }
    }
}