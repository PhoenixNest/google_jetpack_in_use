package com.dev.online_todo_list_example.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dev.online_todo_list_example.data.models.ToDoData

@Dao
interface ToDoDao {
    @Query("select * from todo_table order by id asc")
    fun getAllData(): LiveData<List<ToDoData>>

    // If we insert the duplicate data, let the DB choose ignore and force insert it.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    // Use "suspend" keyword to let the system know this is an Kotlin coroutine function.
    // is recommend to use "suspend" keyword in all the DB function
    suspend fun insertData(toDoData: ToDoData)

    @Update
    suspend fun updateData(toDoData: ToDoData)

    @Delete
    suspend fun deleteItem(toDoData: ToDoData)

    @Query("delete from todo_table")
    suspend fun deleteAll()
}