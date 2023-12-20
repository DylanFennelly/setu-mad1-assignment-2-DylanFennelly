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
    var level: String = "1",        //character level, all characters start at lvl 1 and max 20
    var str: String = "10",             //strength score, min 1 and max 30
    var dex: String = "10",             //dexterity score
    var con: String = "10",             //constitution score
    var int: String = "10",             //intelligence
    var wis: String = "10",             //wisdom
    var cha: String = "10",             //charisma
    var maxHP: String = "0",           //max hit points, calculated from battleClass and constitution score
    var ac: String = "10",              //armour class
    var background: String = "",     //character background
)


// Extension function to convert [CharacterDetails] to [CharacterModel]
fun CharacterDetails.toCharacter(): CharacterModel = CharacterModel(
    id = id,
    name = name,
    race = race,
    battleClass = battleClass,
    level = level.toByteOrNull() ?: 1,
    str = str.toByteOrNull() ?: 10,
    dex = dex.toByteOrNull() ?: 10,
    con = con.toByteOrNull() ?: 10,
    int = int.toByteOrNull() ?: 10,
    wis = wis.toByteOrNull() ?: 10,
    cha = cha.toByteOrNull() ?: 10,
    maxHP = maxHP.toShortOrNull() ?: 0,
    ac = ac.toByteOrNull() ?: 10,
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
    level = level.toString(),
    str = str.toString(),
    dex = dex.toString(),
    con = con.toString(),
    int = int.toString(),
    wis = wis.toString(),
    cha = cha.toString(),
    maxHP = maxHP.toString(),
    ac = ac.toString(),
    background = background
)