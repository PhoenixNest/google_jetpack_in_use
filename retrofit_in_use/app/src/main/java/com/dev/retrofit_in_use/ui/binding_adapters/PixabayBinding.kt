package com.dev.retrofit_in_use.ui.binding_adapters

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.dev.retrofit_in_use.R
import com.dev.retrofit_in_use.models.Hit
import com.dev.retrofit_in_use.ui.fragments.pixabay.PixabayFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PixabayBinding {

    companion object {

        /* ======================== Pixabay RV ======================== */

        @BindingAdapter("android:loadImageFromResponse")
        @JvmStatic
        fun loadImageFromResponse(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        @BindingAdapter("android:sendDataToDetailFragment")
        @JvmStatic
        // RV onItemClickListener
        fun sendDataToDetailFragment(itemView: ConstraintLayout, currentItem: Hit) {
            itemView.setOnClickListener {
                val action =
                    PixabayFragmentDirections.actionPixabayFragmentToDetailsFragment(currentItem)
                itemView.findNavController().navigate(action)
            }
        }
    }
}