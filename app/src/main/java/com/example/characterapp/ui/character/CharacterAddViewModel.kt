package com.example.characterapp.ui.character

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.characterapp.data.CharacterModel
import com.example.characterapp.data.CharacterRepository


//ViewModel to validate and insert items in the Room database.
//TODO: Add room database

class CharacterAddViewModel(private val characterRepository: CharacterRepository): ViewModel() {
    var characterUiState by mutableStateOf(CharacterUiState())
        private set

    fun updateUiState(characterDetails: CharacterDetails){
        characterUiState = CharacterUiState(characterDetails, isEntryValid = validateInput(characterDetails))
    }

    private fun validateInput(uiState: CharacterDetails = characterUiState.characterDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && race.isNotBlank() && battleClass.isNotBlank() && background.isNotBlank()
        }
    }

    suspend fun saveCharacter(){
        if (validateInput()){
            characterRepository.addCharacter(characterUiState.characterDetails.toCharacter())
        }
    }

}

//Holds UI state of item (state of item in UI elements)
data class CharacterUiState(
    val characterDetails: CharacterDetails = CharacterDetails(),
    val isEntryValid: Boolean = false
)

data class CharacterDetails(
    var id: Long = 0,
    var name: String = "",          //name of character
    var race: String = "",          //race
    var battleClass: String = "",   //class of character -> class is reserved word
    var level: Byte = 1,            //character level, all characters start at lvl 1 and max 20
    var str: Byte = 10,             //strength score, min 1 and max 30
    var dex: Byte = 10,             //dexterity score
    var con: Byte = 10,             //constitution score
    var int: Byte = 10,             //intelligence
    var wis: Byte = 10,             //wisdom
    var cha: Byte = 10,             //charisma
    var maxHP: Short = 0,           //max hit points, calculated from battleClass and constitution score
    var ac: Byte = 10,              //armour class
    var background: String = "",     //character background
)


// Extension function to convert [CharacterDetails] to [CharacterModel]
fun CharacterDetails.toCharacter(): CharacterModel = CharacterModel(
    id = id,
    name = name,
    race = race,
    battleClass = battleClass,
    level = level,
    str = str,
    dex = dex,
    con = con,
    int = int,
    wis = wis,
    cha = cha,
    maxHP = maxHP,
    ac = ac,
    background = background
)


//Extension function to convert [CharacterModel] to [CharacterUiState]
fun CharacterModel.toCharacterUiState(isEntryValid: Boolean = false): CharacterUiState = CharacterUiState(
    characterDetails = this.toCharacterDetails(),
    isEntryValid = isEntryValid
)

//Extension function to convert [CharacterModel] to [CharacterDetails]

fun CharacterModel.toCharacterDetails(): CharacterDetails = CharacterDetails(
    id = id,
    name = name,
    race = race,
    battleClass = battleClass,
    level = level,
    str = str,
    dex = dex,
    con = con,
    int = int,
    wis = wis,
    cha = cha,
    maxHP = maxHP,
    ac = ac,
    background = background
)