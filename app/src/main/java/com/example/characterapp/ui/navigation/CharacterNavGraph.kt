package com.example.characterapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.characterapp.ui.character.CharacterAddDestination
import com.example.characterapp.ui.character.CharacterAddScreen
import com.example.characterapp.ui.home.HomeScreen
import com.example.characterapp.ui.home.HomeDestination

@Composable
fun CharacterNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier= modifier
    ){
        composable(route =  HomeDestination.route){
            HomeScreen(
                navigateToCreateCharacter = {navController.navigate(CharacterAddDestination.route)},
                navigateToItemUpdate = {}
            )
        }

        composable(route = CharacterAddDestination.route){
            CharacterAddScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() })
        }
    }
}