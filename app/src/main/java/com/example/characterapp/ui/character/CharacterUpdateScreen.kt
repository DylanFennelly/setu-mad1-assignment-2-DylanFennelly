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
    val raceOptions = listOf(
        stringResource(R.string.race_dragonborn),
        stringResource(R.string.race_dwarf),
        stringResource(R.string.race_elf),
        stringResource(R.string.race_gnome),
        stringResource(R.string.race_halfelf),
        stringResource(R.string.race_halforc),
        stringResource(R.string.race_halfling),
        stringResource(R.string.race_human),
        stringResource(R.string.race_tiefling),
    )
    val bgOptions = listOf(
        stringResource(R.string.bg_acolyte),
        stringResource(R.string.bg_charlatan),
        stringResource(R.string.bg_criminal),
        stringResource(R.string.bg_entertainer),
        stringResource(R.string.bg_folk_hero),
        stringResource(R.string.bg_guild_artisan),
        stringResource(R.string.bg_hermit),
        stringResource(R.string.bg_noble),
        stringResource(R.string.bg_outlander),
        stringResource(R.string.bg_sage),
        stringResource(R.string.bg_sailor),
        stringResource(R.string.bg_solider),
        stringResource(R.string.bg_urchin),
    )
    val classOptions = listOf(
        stringResource(R.string.class_barbarian),
        stringResource(R.string.class_bard),
        stringResource(R.string.class_cleric),
        stringResource(R.string.class_druid),
        stringResource(R.string.class_fighter),
        stringResource(R.string.class_monk),
        stringResource(R.string.class_paladin),
        stringResource(R.string.class_ranger),
        stringResource(R.string.class_rouge),
        stringResource(R.string.class_sorcerer),
        stringResource(R.string.class_warlock),
        stringResource(R.string.class_wizard),
    )
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
    ){innerPadding ->
        CharacterEntryBody(
            characterUiState = viewModel.characterUiState,
            onCharacterValueChange = viewModel::updateUiState,
            onCharacterHPValueChange = viewModel::updateUiStateAndHP,
            onSaveClick = { coroutineScope.launch{
                viewModel.updateItem()
                navigateBack()
            } },
            raceOptions = raceOptions,
            bgOptions = bgOptions,
            classOptions = classOptions,
            isUpdate = true,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )

        if(cancelUpdateConfirmation){
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
