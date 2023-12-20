package com.example.characterapp.helpers

import androidx.compose.ui.res.stringResource
import com.example.characterapp.R
import kotlin.math.floor

val levelPattern = Regex("\\b([1-9]|1[0-9]|20)\\b")       //allows numbers between 1 and 20 - https://www.regextutorial.org/regex-for-numbers-and-ranges.php
val abilityScorePattern = Regex("\\b([1-9]|[1|2][0-9]|30)\\b")       //allows numbers between 1 and 30
val acPattern = Regex("\\b([1-9]|[1|2|3|4][0-9]|50)\\b")       //allows numbers between 1 and 50 (maximum achievable AC is 49)
//Validate character level input in create or edit screens
fun validateLevelInput(charLevel: String): Boolean{
    return charLevel.isNotEmpty() && levelPattern.matches(charLevel)
}

//Validate ability score inputs in create or edit screens
fun validateAbilityScoreInput(abilityScore: String): Boolean{
    return abilityScore.isNotEmpty() && abilityScorePattern.matches(abilityScore)
}

//Validate armour class inputs in create or edit screens
fun validateACInput(ac: String): Boolean{
    return ac.isNotEmpty() && acPattern.matches(ac)
}

// Calculates the ability score modifier from an input score.
fun calculateMod(abilityScore: String) : String{
    return if(validateAbilityScoreInput(abilityScore)) {
            var mod: Float =
                abilityScore.toFloat()        //mod value can end up with a decimal due to division
            mod =
                floor((mod - 10) / 2)                  //10 is taken away, then divided by 2, then rounded down

            return mod.toInt().toString()
    }else{
        "0"
    }
}

// Takes in character level, class, and con score to calculate max HP
// There is an issue in regards to accessibility here, as the names are hardcoded as their English counterparts. However, there was no
// way to access the data inside the string resource without being inside a composable, which this utility is not.
fun calculateHP(charLevel : String, charClass : String, conScore : String): Short{
    var maxHP : Int = 0

    //Base HP (level 1): Class HP + con mod
    when(charClass){
        "Barbarian" -> maxHP += (12 + calculateMod(conScore).toInt())
        "Bard" -> maxHP += (8 + calculateMod(conScore).toInt())
        "Cleric" -> maxHP += (8 + calculateMod(conScore).toInt())
        "Druid"  ->maxHP += (8 + calculateMod(conScore).toInt())
        "Fighter"  ->maxHP += (10 + calculateMod(conScore).toInt())
        "Monk"  -> maxHP += (8 + calculateMod(conScore).toInt())
        "Paladin"  -> maxHP += (10 + calculateMod(conScore).toInt())
        "Ranger"  -> maxHP += (10 + calculateMod(conScore).toInt())
        "Rouge"  -> maxHP += (8 + calculateMod(conScore).toInt())
        "Sorcerer"  -> maxHP += (6 + calculateMod(conScore).toInt())
        "Warlock"  -> maxHP += (8 + calculateMod(conScore).toInt())
        "Wizard"  -> maxHP += (6 + calculateMod(conScore).toInt())
        else -> maxHP = 0   //for if during character creation con score is changed before class is selected
    }

    //HP above level 1: (Class HP + con mod) x levels above 1
    if (validateLevelInput(charLevel)) {
        if (charLevel.toIntOrNull()!! >= 2) {
            var hpPerLevel: Int = 0
            when (charClass) {
                "Barbarian" -> hpPerLevel += (7 + calculateMod(conScore).toInt())
                "Bard" -> hpPerLevel += (5 + calculateMod(conScore).toInt())
                "Cleric" -> hpPerLevel += (5 + calculateMod(conScore).toInt())
                "Druid" -> hpPerLevel += (5 + calculateMod(conScore).toInt())
                "Fighter" -> hpPerLevel += (6 + calculateMod(conScore).toInt())
                "Monk" -> hpPerLevel += (5 + calculateMod(conScore).toInt())
                "Paladin" -> hpPerLevel += (6 + calculateMod(conScore).toInt())
                "Ranger" -> hpPerLevel += (6 + calculateMod(conScore).toInt())
                "Rouge" -> hpPerLevel += (5 + calculateMod(conScore).toInt())
                "Sorcerer" -> hpPerLevel += (4 + calculateMod(conScore).toInt())
                "Warlock" -> hpPerLevel += (5 + calculateMod(conScore).toInt())
                "Wizard" -> hpPerLevel += (4 + calculateMod(conScore).toInt())
                else -> hpPerLevel = 1
            }

            if (hpPerLevel < 1) {    //if character would gain no HP/lose hp on level up, guarantee 1 HP gain
                hpPerLevel = 1
            }
            maxHP += hpPerLevel * (charLevel.toInt() - 1) //multiply by levels above 1 (i.e. level 5 character has base HP + hpPerLevel * 4)
        }
    }

    return maxHP.toShort();
}