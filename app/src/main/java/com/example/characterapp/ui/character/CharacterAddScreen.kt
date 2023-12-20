package com.example.characterapp.ui.character

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.characterapp.CharacterTopAppBar
import com.example.characterapp.R
import com.example.characterapp.ui.AppViewModelProvider
import com.example.characterapp.ui.navigation.NavigationDestination

object CharacterAddDestination: NavigationDestination {
    override val route = "add_character"
    override val titleRes = R.string.add_character
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterAddScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    characterViewModel: CharacterAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    //TODO: define elsewhere?
    val raceOptions = listOf<String>(
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

    Scaffold(
        topBar = {
            CharacterTopAppBar(
                title = stringResource(CharacterAddDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    )
    { innerPadding ->
        CharacterEntryBody(
            characterUiState = characterViewModel.characterUiState,
            onCharacterValueChange = characterViewModel::updateUiState,
            onSaveClick = { /*TODO*/ },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            raceOptions = raceOptions
        )
    }
}

@Composable
fun CharacterEntryBody(
    characterUiState: CharacterUiState,
    onCharacterValueChange: (CharacterDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    raceOptions: List<String>
){
  Column (
      verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
      modifier = Modifier.padding(top = 76.dp, start = 16.dp, end = 16.dp),
  ){
      CharacterInputForm(
          characterDetails = characterUiState.characterDetails,
          onValueChange =  onCharacterValueChange,
          modifier = Modifier.fillMaxWidth(),
          raceOptions = raceOptions
      )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterInputForm(
    characterDetails: CharacterDetails,
    modifier: Modifier = Modifier,
    onValueChange: (CharacterDetails) -> Unit = {},
    raceOptions: List<String>,
    enabled: Boolean = true
){
    var expanded by remember { mutableStateOf(false)}   //state of dropdown menu (if it is open or not)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ){

        OutlinedTextField(      //https://alexzh.com/jetpack-compose-dropdownmenu/
            value = characterDetails.name,
            onValueChange = { onValueChange(characterDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.chara_name_label)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        //https://alexzh.com/jetpack-compose-dropdownmenu/
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded // flip state of menu open/closed
            }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor() // defines where the dropdown menu should appear - https://semicolonspace.com/jetpack-compose-dropdown-menu-material3/
                    .fillMaxWidth(),
                readOnly = true,
                value = characterDetails.race,
                onValueChange = {},
                label = { Text(stringResource(R.string.race_dropdown_label)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )

            // menu
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false        //when menu is dismissed, close menu
                },
            ) {
                // menu items
                raceOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            characterDetails.race = option
                            expanded = false
                        }
                    )
                }
            }
        }

    }
}