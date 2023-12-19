package com.example.characterapp.data

class InMemoryCharacterRepository : CharacterRepository {
    private var characterList = mutableListOf<CharacterModel>()

    override fun getTodos(): List<CharacterModel> = characterList.toList()

    override fun addCharacter(characterModel: CharacterModel) {
        characterList.add(characterModel)
    }

    override fun updateTodo(todo: CharacterModel) {
        val index = characterList.indexOfFirst { it.id == todo.id }
        if (index != -1) {
            characterList[index] = todo
        }
    }
}