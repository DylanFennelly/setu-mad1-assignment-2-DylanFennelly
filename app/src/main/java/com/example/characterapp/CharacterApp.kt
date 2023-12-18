package com.example.characterapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.characterapp.ui.navigation.CharacterNavHost

/**
 * Top level composable that represents screens for the application.
 */
@Composable
fun CharacterApp(navController: NavHostController = rememberNavController()){
    CharacterNavHost(navController = navController)
}