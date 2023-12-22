package com.example.characterapp.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.characterapp.CharacterApplication
import com.example.characterapp.ui.character.CharacterAddViewModel
import com.example.characterapp.ui.character.CharacterDetailsViewModel
import com.example.characterapp.ui.character.CharacterUpdateViewModel
import com.example.characterapp.ui.home.HomeViewModel

/**
* Provides Factory to create instance of ViewModel for the entire app
*/
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(characterApplication().container.characterRepository)
        }

        initializer {
            CharacterAddViewModel(characterApplication().container.characterRepository)
        }

        initializer {
            CharacterDetailsViewModel(
                this.createSavedStateHandle(),
                characterApplication().container.characterRepository
            )
        }
        initializer {
            CharacterUpdateViewModel(
                this.createSavedStateHandle(),
                characterApplication().container.characterRepository
            )
        }
    }
}


/**
 * Extension function to queries for [Application] object and returns an instance of
 * [CharacterApplication].
 */
fun CreationExtras.characterApplication(): CharacterApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CharacterApplication)