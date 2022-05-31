package com.dev.retrofit_in_use.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

class WidgetUtil {
    companion object {

        // Show Permission Snackbar
        fun View.showSnackbar(
            view: View,
            msg: String,
            length: Int,
            actionMessage: CharSequence?,
            action: (View) -> Unit
        ) {
            val snackBar = Snackbar.make(view, msg, length)
            if (actionMessage != null) {
                snackBar.setAction(actionMessage) {
                    action(this)
                }.show()
            } else {
                snackBar.show()
            }
        }

    }
}