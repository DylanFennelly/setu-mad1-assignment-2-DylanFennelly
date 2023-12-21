package com.example.characterapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.characterapp.data.CharacterDao
import com.example.characterapp.data.CharacterDatabase
import com.example.characterapp.data.CharacterModel
import com.example.characterapp.helpers.calculateHP
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

//Tests for CharacterDao functions
@RunWith(AndroidJUnit4::class)
class CharacterDaoTest {
    private lateinit var characterDao: CharacterDao
    private lateinit var characterDatabase: CharacterDatabase

    private var character1 = CharacterModel(1, "Cyn", "Half-Elf", "Ranger",2, 12, 16, 13, 8, 15, 10, calculateHP("2", "Ranger", "13"), ac = 15, background = "Criminal")
    private var character2 = CharacterModel(2, "Leona", "Human", "Sorcerer",5, 7, 13, 15, 16, 14, 20, calculateHP("5", "Sorcerer", "15"), ac = 13, background = "Sage")

    private suspend fun addOneCharacterToDb() {
        characterDao.insert(character1)
    }

    private suspend fun addTwoCharactersToDb() {
        characterDao.insert(character1)
        characterDao.insert(character2)
    }

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        characterDatabase = Room.inMemoryDatabaseBuilder(context, CharacterDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        characterDao = characterDatabase.itemDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        characterDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsCharacterIntoDB() = runBlocking {
        addOneCharacterToDb()
        val allCharacters = characterDao.getAllCharacters().first()
        assertEquals(allCharacters[0], character1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllCharactersFromDB() = runBlocking {
        addTwoCharactersToDb()
        val allCharacters = characterDao.getAllCharacters().first()
        assertEquals(allCharacters[0], character1)
        assertEquals(allCharacters[1], character2)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateItems_updatesCharactersInDB() = runBlocking {
        addTwoCharactersToDb()
        characterDao.update(CharacterModel(1, "Cyn", "Half-Elf", "Ranger",3, 12, 16, 14, 8, 16, 10, calculateHP("3", "Ranger", "14"), ac = 16, background = "Criminal"))
        characterDao.update(CharacterModel(2, "Leona", "Human", "Cleric",5, 7, 13, 15, 16, 20, 14, calculateHP("5", "Cleric", "15"), ac = 14, background = "Sage"))

        val allCharacters = characterDao.getAllCharacters().first()
        assertEquals(allCharacters[0], CharacterModel(1, "Cyn", "Half-Elf", "Ranger",3, 12, 16, 14, 8, 16, 10, calculateHP("3", "Ranger", "14"), ac = 16, background = "Criminal"))
        assertEquals(allCharacters[1], CharacterModel(2, "Leona", "Human", "Cleric",5, 7, 13, 15, 16, 20, 14, calculateHP("5", "Cleric", "15"), ac = 14, background = "Sage"))
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deletesAllCharactersFromDB() = runBlocking {
        addTwoCharactersToDb()
        characterDao.delete(character1)
        characterDao.delete(character2)
        val allCharacters = characterDao.getAllCharacters().first()
        assertTrue(allCharacters.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsCharacterFromDB() = runBlocking {
        addOneCharacterToDb()
        val character = characterDao.getCharacter(1)
        assertEquals(character.first(), character1)
    }

}