package com.example.characterapp.data

interface TodoRepository {
    fun getTodos(): List<TodoItem>
    fun addTodo(todo: TodoItem)
    fun updateTodo(todo: TodoItem)
}