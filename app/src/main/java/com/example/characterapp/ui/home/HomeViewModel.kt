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

    fun addCharacter(task: String) {
        val newTodo = CharacterModel(id = (characters.value.size + 1).toLong())     //other values
        repository.addCharacter(newTodo)
        refreshCharacters()
    }

    fun toggleTodoStatus(todo: CharacterModel) {
//        val updatedTodo = todo.copy(isCompleted = !todo.isCompleted)
//        repository.updateTodo(updatedTodo)
        refreshCharacters()
    }

    private fun refreshCharacters() {
        characters.value = repository.getTodos()
    }
}
