package com.example.characterapp.helpers

import kotlin.math.floor

val levelPattern = Regex("\\b([1-9]|1[0-9]|20)\\b")       //allows numbers between 1 and 20 - https://www.regextutorial.org/regex-for-numbers-and-ranges.php
val abilityScorePattern = Regex("\\b([1-9]|[1|2][0-9]|30)\\b")       //allows numbers between 1 and 30
//Validate character level input in create or edit screens
fun validateLevelInput(charLevel: String): Boolean{
    return charLevel.isNotEmpty() && levelPattern.matches(charLevel)
}

//Validate ability score inputs in create or edit screens
fun validateAbilityScoreInput(abilityScore: String): Boolean{
    return abilityScore.isNotEmpty() && abilityScorePattern.matches(abilityScore)
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