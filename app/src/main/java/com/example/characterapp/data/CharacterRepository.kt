package com.example.characterapp.data

interface CharacterRepository {
    fun getTodos(): List<CharacterModel>
    fun addCharacter(characterModel: CharacterModel)
    fun updateTodo(todo: CharacterModel)
}