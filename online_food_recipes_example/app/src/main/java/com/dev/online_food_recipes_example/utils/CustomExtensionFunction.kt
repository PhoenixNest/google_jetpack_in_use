package com.dev.online_food_recipes_example.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

// Custom Function to Fix the Fetch-Data-Twice-Call
private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            // The Data is not from the Local access is obtained from the Cloud
            // we just need Observe the LiveData Object only Once and not Every-Time
            removeObserver(this)
            observer.onChanged(t)
        }
    })
}
