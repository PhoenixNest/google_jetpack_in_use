package com.dev.online_todo_list_example.data

import androidx.room.TypeConverter
import com.dev.online_todo_list_example.data.models.Priority

class Converter {
    // Because of the Database can't save the class type value directly,
    // when the data is inserted into the Database,
    // we should convert the Priority::class into String first

    // Priority -> String
    @TypeConverter
    fun fromPriority(priority: Priority): String {
        // Return the Name of the Enum

        // HIGH, MEDIUM, LOW
        return priority.name
    }

    // String -> Priority
    @TypeConverter
    fun toPriority(priority: String): Priority {
        // Return the enum which match the pass priority value

        // Such as High, Medium, Low
        return Priority.valueOf(priority)
    }
}