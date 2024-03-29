package com.example.characterapp.data

import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    //Retrieve all the characters from the the given data source.
    fun getAllCharactersStream(): Flow<List<CharacterModel>>

    //Retrieve specific character
    fun getCharacterStream(id: Long): Flow<CharacterModel?>

    //insert character
    suspend fun insertCharacter(characterModel: CharacterModel)

    //delete character
    suspend fun deleteCharacter(characterModel: CharacterModel)

    //update character
    suspend fun updateCharacter(characterModel: CharacterModel)
}