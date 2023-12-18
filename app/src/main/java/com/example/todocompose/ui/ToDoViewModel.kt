package com.example.todocompose.ui

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.todocompose.data.TodoItem
import com.example.todocompose.data.TodoRepository
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todocompose.ToDoApplication

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    val todos = mutableStateOf<List<TodoItem>>(emptyList())

    init {
        refreshTodos()
    }

    fun addTodo(task: String) {
        val newTodo = TodoItem(id = todos.value.size + 1, task = task)
        repository.addTodo(newTodo)
        refreshTodos()
    }

    fun toggleTodoStatus(todo: TodoItem) {
        val updatedTodo = todo.copy(isCompleted = !todo.isCompleted)
        repository.updateTodo(updatedTodo)
        refreshTodos()
    }

    private fun refreshTodos() {
        todos.value = repository.getTodos()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ToDoApplication)
                val todoRepository = application.container.todoRepository
                TodoViewModel(repository = todoRepository)
            }
        }
    }
}
