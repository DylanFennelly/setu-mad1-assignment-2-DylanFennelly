package com.example.characterapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.characterapp.ui.CharacterListScreen
import com.example.characterapp.ui.HomeDestination

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
            CharacterListScreen(
                navigateToItemEntry = {},
                navigateToItemUpdate = {}
            )
        }
    }
}