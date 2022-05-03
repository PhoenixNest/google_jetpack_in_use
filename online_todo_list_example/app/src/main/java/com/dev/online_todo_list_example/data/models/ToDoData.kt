package com.dev.online_todo_list_example.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "todo_table")
// in order to pass the object value into fragment, we yse safe args
// so change to Data object into Parcelable that we can pass it
@Parcelize
data class ToDoData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var title: String,
    var priority: Priority,
    var description: String
) : Parcelable
