package com.example.characterapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.characterapp.data.CharacterModel
import com.example.characterapp.data.CharacterRepository
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.characterapp.CharacterApplication
import com.example.characterapp.ui.character.CharacterAddViewModel

class HomeViewModel(private val repository: CharacterRepository) : ViewModel() {
    val characters = mutableStateOf<List<CharacterModel>>(emptyList())

    init {
        refreshCharacters()
    }

    fun refreshCharacters() {
        characters.value = repository.getCharacters()
    }
}
