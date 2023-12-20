package com.example.characterapp.data

interface CharacterRepository {
    fun getCharacters(): List<CharacterModel>
    fun addCharacter(characterModel: CharacterModel)
    fun updateTodo(todo: CharacterModel)
}