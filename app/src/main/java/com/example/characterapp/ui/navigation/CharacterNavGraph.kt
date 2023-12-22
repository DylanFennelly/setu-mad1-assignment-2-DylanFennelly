package com.example.characterapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.characterapp.ui.character.CharacterAddDestination
import com.example.characterapp.ui.character.CharacterAddScreen
import com.example.characterapp.ui.character.CharacterDetailsDestination
import com.example.characterapp.ui.character.CharacterDetailsScreen
import com.example.characterapp.ui.character.CharacterUpdateDestination
import com.example.characterapp.ui.character.CharacterUpdateScreen
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
                navigateToCharacterDetails = { navController.navigate("${CharacterDetailsDestination.route}/${it}") }
            )
        }

        composable(route = CharacterAddDestination.route){
            CharacterAddScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() })
        }

        composable(
            route = CharacterDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(CharacterDetailsDestination.characterIdArg){
                type = NavType.LongType
            })
        ){
            CharacterDetailsScreen(
                navigateBack = { navController.popBackStack() },
                navigateToUpdateCharacter = { navController.navigate("${CharacterUpdateDestination.route}/${it}") },
                onNavigateUp = { navController.navigateUp() },
                )
        }

        composable(
            route = CharacterUpdateDestination.routeWithArgs,
            arguments = listOf(navArgument(CharacterUpdateDestination.characterIdArg){
                type = NavType.LongType
            })
        ){
            CharacterUpdateScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

    }
}