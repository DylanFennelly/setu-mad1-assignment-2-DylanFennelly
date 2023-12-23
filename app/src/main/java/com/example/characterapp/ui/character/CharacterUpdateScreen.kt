package com.example.characterapp.ui.character

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.characterapp.CharacterTopAppBar
import com.example.characterapp.R
import com.example.characterapp.helpers.characterBackgrounds
import com.example.characterapp.helpers.characterClasses
import com.example.characterapp.helpers.characterRaces
import com.example.characterapp.ui.AppViewModelProvider
import com.example.characterapp.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

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
    val raceOptions = mutableListOf<String>()
    characterRaces.forEach { race ->
        raceOptions.add(stringResource(id = race))
    }

    val bgOptions = mutableListOf<String>()
    characterBackgrounds.forEach { bg ->
        bgOptions.add(stringResource(id = bg))
    }

    val classOptions = mutableListOf<String>()
    characterClasses.forEach { bC ->
        classOptions.add(stringResource(id = bC))
    }

    var cancelUpdateConfirmation by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CharacterTopAppBar(
                title = stringResource(CharacterUpdateDestination.titleRes),
                canNavigateBack = true,
                navigateUp = { cancelUpdateConfirmation = true },
                canDelete = false,
                canUpdate = false,
            )
        },
        modifier = modifier
    ) { innerPadding ->
        CharacterEntryBody(
            characterUiState = viewModel.characterUiState,
            onCharacterValueChange = viewModel::updateUiState,
            onCharacterHPValueChange = viewModel::updateUiStateAndHP,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateItem()
                    navigateBack()
                }
            },
            raceOptions = raceOptions,
            bgOptions = bgOptions,
            classOptions = classOptions,
            isUpdate = true,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )

        if (cancelUpdateConfirmation) {
            AlertDialog(
                onDismissRequest = { cancelUpdateConfirmation = false },
                title = { Text(text = stringResource(R.string.cancel_update_alert_title)) },
                text = {
                    Text(
                        text = stringResource(R.string.cancel_update_alert_desc),
                        textAlign = TextAlign.Center

                    )
                },
                dismissButton = {
                    TextButton(onClick = { cancelUpdateConfirmation = false }) {
                        Text(text = stringResource(R.string.alert_go_back))
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        cancelUpdateConfirmation = false
                        onNavigateUp()
                    }
                    ) {
                        Text(text = stringResource(R.string.alert_confirm))
                    }
                },
                containerColor = colorResource(R.color.alert_red),
            )
        }

    }
}
