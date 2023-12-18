package com.example.characterapp.data

interface CharacterRepository {
    fun getTodos(): List<CharacterModel>
    fun addTodo(todo: CharacterModel)
    fun updateTodo(todo: CharacterModel)
}