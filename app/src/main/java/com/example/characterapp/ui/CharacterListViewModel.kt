package com.example.characterapp.ui

import androidx.lifecycle.ViewModel
import com.example.characterapp.data.CharacterModel
import com.example.characterapp.data.CharacterRepository
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.characterapp.CharacterApplication

class CharacterListViewModel(private val repository: CharacterRepository) : ViewModel() {
    val todos = mutableStateOf<List<CharacterModel>>(emptyList())

    init {
        refreshCharacters()
    }

    fun addCharacter(task: String) {
        val newTodo = CharacterModel(id = todos.value.size + 1, task = task)
        repository.addTodo(newTodo)
        refreshCharacters()
    }

    fun toggleTodoStatus(todo: CharacterModel) {
        val updatedTodo = todo.copy(isCompleted = !todo.isCompleted)
        repository.updateTodo(updatedTodo)
        refreshCharacters()
    }

    private fun refreshCharacters() {
        todos.value = repository.getTodos()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CharacterApplication)
                val todoRepository = application.container.characterRepository
                CharacterListViewModel(repository = todoRepository)
            }
        }
    }
}
