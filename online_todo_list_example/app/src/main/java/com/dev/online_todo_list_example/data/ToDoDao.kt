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

    // Use LIKE operator to search for a specified patten in a column.
    @Query("select * from todo_table where title like :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>>

    // Sort by: High(1) -> Medium(2) -> Low(3)
    // Case when(...) then when(...) ... End
    // which more like the switch or when function in Java and Kotlin.
    @Query("select * from todo_table order by case when priority like 'H%' then 1 when priority like 'M%' then 2 when priority like 'L%' then 3 end")
    fun sortByHighPriority(): LiveData<List<ToDoData>>

    // Sort by: Low(1) -> Medium(2) -> High(3)
    // Case when(...) then when(...) ... End
    // which more like the switch or when function in Java and Kotlin.
    @Query("select * from todo_table order by case when priority like 'L%' then 1 when priority like 'M%' then 2 when priority like 'H%' then 3 end")
    fun sortByLowPriority(): LiveData<List<ToDoData>>
}