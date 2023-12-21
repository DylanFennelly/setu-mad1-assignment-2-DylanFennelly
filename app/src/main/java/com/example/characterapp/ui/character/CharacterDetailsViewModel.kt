package com.example.characterapp.ui.character

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.characterapp.data.CharacterRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val characterRepository: CharacterRepository
) : ViewModel() {
    private val characterId: Long = checkNotNull(savedStateHandle[CharacterDetailsDestination.characterIdArg])

    val uiState: StateFlow<CharacterDetailsUiState> =
        characterRepository.getCharacterStream(characterId)
            .filterNotNull()
            .map {
                CharacterDetailsUiState(characterDetails = it.toCharacterDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CharacterDetailsUiState()
            )

    suspend fun deleteItem() {
        characterRepository.deleteCharacter(uiState.value.characterDetails.toCharacter())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for ItemDetailsScreen
 */
data class CharacterDetailsUiState(
    val characterDetails: CharacterDetails = CharacterDetails()
)