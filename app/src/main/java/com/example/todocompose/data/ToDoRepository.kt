package com.example.todocompose.data

interface TodoRepository {
    fun getTodos(): List<TodoItem>
    fun addTodo(todo: TodoItem)
    fun updateTodo(todo: TodoItem)
}