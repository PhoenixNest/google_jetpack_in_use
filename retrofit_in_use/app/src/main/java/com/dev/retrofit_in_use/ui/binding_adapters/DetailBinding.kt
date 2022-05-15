package com.dev.retrofit_in_use.ui.binding_adapters

import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailBinding {

    companion object {
        @BindingAdapter("android:saveImage")
        @JvmStatic
        fun saveImage(floatingActionButton: FloatingActionButton, imageUrl: String) {
            floatingActionButton.setOnClickListener {

            }
        }
    }
}