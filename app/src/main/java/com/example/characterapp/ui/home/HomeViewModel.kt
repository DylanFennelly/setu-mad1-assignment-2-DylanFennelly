package com.example.characterapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.characterapp.data.CharacterModel
import com.example.characterapp.data.CharacterRepository
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.characterapp.CharacterApplication

class HomeViewModel(private val repository: CharacterRepository) : ViewModel() {
    val characters = mutableStateOf<List<CharacterModel>>(emptyList())

    init {
        refreshCharacters()
    }

    fun addCharacter(task: String) {
        val newTodo = CharacterModel(id = (characters.value.size + 1).toLong())     //other values
        repository.addTodo(newTodo)
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CharacterApplication)
                val characterRepository = application.container.characterRepository
                HomeViewModel(repository = characterRepository)
            }
        }
    }
}
