package com.dev.online_todo_list_example.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dev.online_todo_list_example.data.models.ToDoData

@Dao
interface ToDoDao {
    @Query("select * from todo_table order by id asc")
    fun getAllData(): LiveData<List<ToDoData>>

    // If we insert the duplicate data, let the Database choose ignore and force insert it.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    // Use "suspend" keyword to let the system know this is an Kotlin coroutine function.
    // Recommend to use "suspend" keyword in all the Database function.
    suspend fun insertData(toDoData: ToDoData)

    @Update
    // Use "suspend" keyword to let the system know this is an Kotlin coroutine function.
    // Recommend to use "suspend" keyword in all the Database function.
    suspend fun updateData(toDoData: ToDoData)

    @Delete
    // Use "suspend" keyword to let the system know this is an Kotlin coroutine function.
    // Recommend to use "suspend" keyword in all the Database function.
    suspend fun deleteItem(toDoData: ToDoData)

    @Query("delete from todo_table")
    // Use "suspend" keyword to let the system know this is an Kotlin coroutine function.
    // Recommend to use "suspend" keyword in all the Database function.
    suspend fun deleteAll()
}