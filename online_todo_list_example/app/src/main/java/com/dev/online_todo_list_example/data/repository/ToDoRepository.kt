package com.dev.online_todo_list_example.data.repository

import androidx.lifecycle.LiveData
import com.dev.online_todo_list_example.data.ToDoDao
import com.dev.online_todo_list_example.data.models.ToDoData

// Use Repository to handle the data between ViewModel and Database.
class ToDoRepository(private val toDoDao: ToDoDao) {
    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData) {
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteItem(toDoData: ToDoData) {
        toDoDao.deleteItem(toDoData)
    }

    suspend fun deleteAll() {
        toDoDao.deleteAll()
    }
}