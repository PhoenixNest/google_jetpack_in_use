package com.dev.online_todo_list_example.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dev.online_todo_list_example.data.ToDoDatabase
import com.dev.online_todo_list_example.data.models.ToDoData
import com.dev.online_todo_list_example.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val todoDao = ToDoDatabase.getDatabase(application).todoDao()

    private val repository: ToDoRepository = ToDoRepository(todoDao)

    val getAllData: LiveData<List<ToDoData>> = repository.getAllData

    fun insertData(toDoData: ToDoData) {
        // Use CoroutineScope to process the function in the background.
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoData) {
        // Use CoroutineScope to process the function in the background.
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteItem(toDoData: ToDoData) {
        // Use CoroutineScope to process the function in the background.
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(toDoData)
        }
    }

    fun deleteAll() {
        // Use CoroutineScope to process the function in the background.
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
}