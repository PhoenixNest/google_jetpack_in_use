package com.dev.online_todo_list_example.data

import androidx.room.TypeConverter
import com.dev.online_todo_list_example.data.models.Priority

class Converter {
    // Because of the DB can't save the class type value,
    // so when the data is inserted into the DB,
    // we should convert the Priority::class into String first
    // Priority -> String
    @TypeConverter
    fun fromPriority(priority: Priority): String {
        // return the Name of the enum

        // HIGH, MEDIUM, LOW
        return priority.name
    }

    // String -> Priority
    @TypeConverter
    fun toPriority(priority: String): Priority {
        // return the enum which match the pass priority value

        // such as high, medium, low
        return Priority.valueOf(priority)
    }
}