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

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(true)

    fun checkIfDatabaseEmpty(toDoData: List<ToDoData>) {
        // if the toDoDataList is Empty, that means there are nothing in Database,
        // set the emptyDatabase value to True or False
        emptyDatabase.value = toDoData.isEmpty()
    }

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
                // force change the display Spinner item as TextView,
                // then change its textColor to match HIGH, MEDIUM, LOW level
                0 -> (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.red))

                1 -> (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.yellow))

                2 -> (parent?.getChildAt(0) as TextView)
                    .setTextColor(ContextCompat.getColor(application, R.color.green))
            }
        }
    }

    // check the user input
    fun verifyDataFromUser(title: String, description: String): Boolean {
        return !(title.isEmpty() || description.isEmpty())
    }

    // check the priority level when Add the item
    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW

            else -> Priority.LOW
        }
    }

    // check the priority level when Update the item
    fun parsePriorityToInt(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }
}