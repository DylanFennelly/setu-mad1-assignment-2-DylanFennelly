package com.example.characterapp.ui.character

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.characterapp.CharacterTopAppBar
import com.example.characterapp.R
import com.example.characterapp.ui.AppViewModelProvider
import com.example.characterapp.ui.navigation.NavigationDestination

object CharacterUpdateDestination : NavigationDestination {
    override val route = "character_update"
    override val titleRes = R.string.character_update_title
    const val characterIdArg = "characterId"
    val routeWithArgs = "$route/{$characterIdArg}"
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterUpdateScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterUpdateViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CharacterTopAppBar(
                title = stringResource(CharacterUpdateDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                canDelete = false,
                canUpdate = false,
            )
        },
        modifier = modifier
    ){

    }
}
