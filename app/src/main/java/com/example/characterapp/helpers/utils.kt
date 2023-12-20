package com.example.characterapp.helpers

val levelPattern = Regex("\\b([1-9]|1[0-9]|20)\\b")       //allows numbers between 1 and 20 - https://www.regextutorial.org/regex-for-numbers-and-ranges.php
//Validate character level input in create or edit screens
fun validateLevelInput(charLevel: String): Boolean{
    return charLevel.isNotEmpty() && levelPattern.matches(charLevel)
}