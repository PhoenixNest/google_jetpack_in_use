package com.dev.online_todo_list_example.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun hideKeyboard(activity: Activity) {
    // Get the InputMethodManager in Activity
    val inputMethodManager = activity.getSystemService(
        Context.INPUT_METHOD_SERVICE
    ) as InputMethodManager

    // Located to the Current Focused View
    val currentFocusedView = activity.currentFocus

    // Hide the Keyboard
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}