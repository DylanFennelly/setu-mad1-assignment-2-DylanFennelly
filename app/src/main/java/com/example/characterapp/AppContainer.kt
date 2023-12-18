package com.example.characterapp

import com.example.characterapp.data.InMemoryTodoRepository
import com.example.characterapp.data.TodoRepository

interface AppContainer {
    val todoRepository: TodoRepository
}

class DefaultAppContainer: AppContainer {
    override val todoRepository: TodoRepository by lazy {
        InMemoryTodoRepository()
    }
}