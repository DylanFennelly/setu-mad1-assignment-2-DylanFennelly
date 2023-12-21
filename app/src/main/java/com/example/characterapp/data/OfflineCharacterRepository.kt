package com.example.characterapp.data

import kotlinx.coroutines.flow.Flow

class OfflineCharacterRepository(private val characterDao: CharacterDao): CharacterRepository {
    override fun getCharacters(): List<CharacterModel> {
        TODO("Remove when no longer needed")
    }

    override fun addCharacter(characterModel: CharacterModel) {
        TODO("Remove when no longer needed")
    }

    override fun updateTodo(todo: CharacterModel) {
        TODO("Remove when no longer needed")
    }

    override fun getAllCharactersStream(): Flow<List<CharacterModel>> = characterDao.getAllCharacters()

    override fun getCharacterStream(id: Long): Flow<CharacterModel?> = characterDao.getCharacter(id)

    override suspend fun insertCharacter(characterModel: CharacterModel) = characterDao.insert(characterModel)

    override suspend fun deleteCharacter(characterModel: CharacterModel) = characterDao.delete(characterModel)

    override suspend fun updateCharacter(characterModel: CharacterModel) = characterDao.update(characterModel)
}