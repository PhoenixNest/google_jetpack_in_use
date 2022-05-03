package com.dev.online_todo_list_example.fragments

import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.dev.online_todo_list_example.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

// View Action Binding
class BindingAdapters {
    companion object {
        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
            if (navigate) {
                view.setOnClickListener {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }
    }
}