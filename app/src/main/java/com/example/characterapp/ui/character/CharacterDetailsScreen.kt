package com.example.characterapp.ui.character

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.characterapp.CharacterTopAppBar
import com.example.characterapp.R
import com.example.characterapp.ui.AppViewModelProvider
import com.example.characterapp.ui.navigation.NavigationDestination

object CharacterDetailsDestination : NavigationDestination {
    override val route = "character_details"
    override val titleRes = R.string.character_details_title
    const val characterIdArg = "characterId"
    val routeWithArgs = "$route/{$characterIdArg}"
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    modifier: Modifier = Modifier,
    detailsViewModel: CharacterDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
   Scaffold(
       topBar = {
           CharacterTopAppBar(
               title = stringResource(CharacterDetailsDestination.titleRes),
               canNavigateBack = canNavigateBack,
               navigateUp = onNavigateUp
           )
       }
   ) {

   }

}