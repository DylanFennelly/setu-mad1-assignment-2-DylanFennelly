package com.example.todocompose

import com.example.todocompose.data.InMemoryTodoRepository
import com.example.todocompose.data.TodoRepository

interface AppContainer {
    val todoRepository: TodoRepository
}

class DefaultAppContainer: AppContainer {
    override val todoRepository: TodoRepository by lazy {
        InMemoryTodoRepository()
    }
}