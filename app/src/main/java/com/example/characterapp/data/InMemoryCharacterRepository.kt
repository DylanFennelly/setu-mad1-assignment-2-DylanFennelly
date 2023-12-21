package com.example.characterapp.data

import kotlinx.coroutines.flow.Flow

class InMemoryCharacterRepository : CharacterRepository {
    private var characterList = mutableListOf<CharacterModel>()

    override fun getCharacters(): List<CharacterModel> = characterList.toList()

    override fun addCharacter(characterModel: CharacterModel) {
        characterList.add(characterModel)
    }

    override fun updateTodo(todo: CharacterModel) {
        val index = characterList.indexOfFirst { it.id == todo.id }
        if (index != -1) {
            characterList[index] = todo
        }
    }

    override fun getAllCharactersStream(): Flow<List<CharacterModel>> {
        TODO("Not yet implemented")
    }

    override fun getCharacterStream(id: Long): Flow<CharacterModel?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCharacter(characterModel: CharacterModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCharacter(characterModel: CharacterModel) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCharacter(characterModel: CharacterModel) {
        TODO("Not yet implemented")
    }
}