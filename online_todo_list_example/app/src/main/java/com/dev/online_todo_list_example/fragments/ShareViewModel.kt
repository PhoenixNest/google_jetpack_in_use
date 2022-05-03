package com.dev.online_todo_list_example.fragments

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dev.online_todo_list_example.R
import com.dev.online_todo_list_example.data.models.Priority
import com.dev.online_todo_list_example.data.models.ToDoData

class ShareViewModel(application: Application) : AndroidViewModel(application) {

    /* ======================== ListFragment ======================== */

    val isDatabaseEmpty: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseEmpty(toDoData: List<ToDoData>) {
        // If the toDoDataList is Empty, that means there are nothing in Database,
        // Set the emptyDatabase value to True or False
        isDatabaseEmpty.value = toDoData.isEmpty()
    }

    /* ======================== Add/Update Fragment ======================== */

    // Spinner: OnItemSelectedListener
    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {}

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            when (position) {
                // Force change the display Spinner item as TextView,
                // Then change its textColor to match HIGH, MEDIUM, LOW level
                0 -> (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.red))

                1 -> (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.yellow))

                2 -> (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.green))
            }
        }
    }

    // Check the user Input
    fun verifyDataFromUser(title: String, description: String): Boolean {
        return !(title.isEmpty() || description.isEmpty())
    }

    // Check the Priority level when Add the item into Database
    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW

            else -> Priority.LOW
        }
    }
}