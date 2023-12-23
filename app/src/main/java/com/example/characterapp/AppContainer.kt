package com.example.characterapp

import android.content.Context
import com.example.characterapp.data.CharacterDatabase
import com.example.characterapp.data.CharacterRepository
import com.example.characterapp.data.OfflineCharacterRepository

interface AppContainer {
    val characterRepository: CharacterRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val characterRepository: CharacterRepository by lazy {
        OfflineCharacterRepository(CharacterDatabase.getDatabase(context).itemDao())
    }
}