package com.example.characterapp.ui.character

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.characterapp.data.CharacterRepository
import com.example.characterapp.helpers.calculateHP
import com.example.characterapp.helpers.validateACInput
import com.example.characterapp.helpers.validateAbilityScoreInput
import com.example.characterapp.helpers.validateLevelInput
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CharacterUpdateViewModel(
    savedStateHandle: SavedStateHandle,
    private val characterRepository: CharacterRepository
) : ViewModel() {
    var characterUiState by mutableStateOf(CharacterUiState())
        private set

    private val characterId: Long = checkNotNull(savedStateHandle[CharacterUpdateDestination.characterIdArg])

    init {
        viewModelScope.launch {
            characterUiState = characterRepository.getCharacterStream(characterId)
                .filterNotNull()
                .first()
                .toCharacterUiState(true)
        }
    }


    private fun validateInput(uiState: CharacterDetails = characterUiState.characterDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() &&
            race.isNotBlank() &&
            battleClass.isNotBlank() &&
            background.isNotBlank() &&
            validateLevelInput(level) &&
            validateAbilityScoreInput(str) &&
            validateAbilityScoreInput(dex) &&
            validateAbilityScoreInput(con) &&
            validateAbilityScoreInput(int) &&
            validateAbilityScoreInput(wis) &&
            validateAbilityScoreInput(cha) &&
            validateACInput(ac)
        }
    }

    fun updateUiState(characterDetails: CharacterDetails) {
        characterUiState =
            CharacterUiState(characterDetails = characterDetails, isEntryValid = validateInput(characterDetails))
    }

    fun updateUiStateAndHP(characterDetails: CharacterDetails){
        characterUiState = CharacterUiState(characterDetails, isEntryValid = validateInput(characterDetails))
        characterDetails.maxHP = calculateHP(characterDetails.level, characterDetails.battleClass, characterDetails.con).toString()
    }

    suspend fun updateItem() {
        if (validateInput(characterUiState.characterDetails)) {
            characterRepository.updateCharacter(characterUiState.characterDetails.toCharacter())
        }
    }
}