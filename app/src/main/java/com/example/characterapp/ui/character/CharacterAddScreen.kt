package com.example.characterapp.ui.character

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
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
            raceOptions = raceOptions, bgOptions = bgOptions, classOptions = classOptions
        )
    }
}

@Composable
fun CharacterEntryBody(
    characterUiState: CharacterUiState,
    onCharacterValueChange: (CharacterDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    raceOptions: List<String>,
    bgOptions: List<String>,
    classOptions: List<String>,
){
  Column (
      verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
      modifier = Modifier.padding(top = 76.dp, start = 16.dp, end = 16.dp),
  ){
      CharacterInputForm(
          characterDetails = characterUiState.characterDetails,
          onValueChange =  onCharacterValueChange,
          modifier = Modifier.fillMaxWidth(),
          raceOptions = raceOptions, bgOptions = bgOptions, classOptions = classOptions
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
    bgOptions: List<String>,
    classOptions: List<String>,
    enabled: Boolean = true
){
    var raceExpanded by remember { mutableStateOf(false)}   //state of dropdown menu (if it is open or not)
    var bgExpanded by remember { mutableStateOf(false)}
    var classExpanded by remember { mutableStateOf(false)}

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ){
        //Name
        OutlinedTextField(      //https://alexzh.com/jetpack-compose-dropdownmenu/
            value = characterDetails.name,
            onValueChange = { onValueChange(characterDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.chara_name_label)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        //Race
        //https://alexzh.com/jetpack-compose-dropdownmenu/
        ExposedDropdownMenuBox(
            expanded = raceExpanded,
            onExpandedChange = {
                raceExpanded = !raceExpanded // flip state of menu open/closed
            }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor() // defines where the dropdown menu should appear - https://semicolonspace.com/jetpack-compose-dropdown-menu-material3/
                    .fillMaxWidth(),
                readOnly = true,
                value = characterDetails.race,
                onValueChange = {},
                label = { Text(stringResource(R.string.chara_race_dropdown_label)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = raceExpanded) },
            )

            ExposedDropdownMenu(
                expanded = raceExpanded,
                onDismissRequest = {
                    raceExpanded = false        //when menu is dismissed, close menu
                },
            ) {
                // menu items
                raceOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            characterDetails.race = option
                            raceExpanded = false
                        }
                    )
                }
            }
        }

        //background
        ExposedDropdownMenuBox(
            expanded = bgExpanded,
            onExpandedChange = {
                bgExpanded = !bgExpanded
            }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = characterDetails.background,
                onValueChange = {},
                label = { Text(stringResource(R.string.chara_bg_dropdown_label)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bgExpanded) },
            )

            ExposedDropdownMenu(
                expanded = bgExpanded,
                onDismissRequest = {
                    bgExpanded = false        //when menu is dismissed, close menu
                },
            ) {
                bgOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            characterDetails.background = option
                            bgExpanded = false
                        }
                    )
                }
            }
        }

        //class
        ExposedDropdownMenuBox(
            expanded = classExpanded,
            onExpandedChange = {
                classExpanded = !classExpanded
            }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = characterDetails.battleClass,
                onValueChange = {},
                label = { Text(stringResource(R.string.chara_class_dropdown_label)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = classExpanded) },
            )

            ExposedDropdownMenu(
                expanded = classExpanded,
                onDismissRequest = {
                    classExpanded = false        //when menu is dismissed, close menu
                },
            ) {
                classOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            characterDetails.battleClass = option
                            classExpanded = false
                        }
                    )
                }
            }
        }

        //Level
        OutlinedTextField(
            value = characterDetails.level,
            onValueChange = { onValueChange(characterDetails.copy(level = it)) },
            label = { Text(stringResource(R.string.chara_level_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            isError = characterDetails.level.isEmpty()
        )

    }
}