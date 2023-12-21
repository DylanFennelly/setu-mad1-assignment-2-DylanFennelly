package com.example.characterapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)     //ignores inserted conflict rows, returns ID -1
    suspend fun insert(characterModel: CharacterModel)

    @Update
    suspend fun update(characterModel: CharacterModel)

    @Delete
    suspend fun delete(characterModel: CharacterModel)

    @Query("SELECT * from characters WHERE id = :id")
    fun getCharacter(id: Int): Flow<CharacterModel>     //Flow auto updates with updates asynchronously

    @Query("SELECT * from characters ORDER BY name ASC")
    fun getAllCharacters(): Flow<List<CharacterModel>>
}