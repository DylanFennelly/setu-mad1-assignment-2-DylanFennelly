package com.example.characterapp

import com.example.characterapp.data.InMemoryCharacterRepository
import com.example.characterapp.data.CharacterRepository

interface AppContainer {
    val characterRepository: CharacterRepository
}

class DefaultAppContainer: AppContainer {
    override val characterRepository: CharacterRepository by lazy {
        InMemoryCharacterRepository()
    }
}