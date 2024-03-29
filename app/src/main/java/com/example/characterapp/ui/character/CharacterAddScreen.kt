package com.example.characterapp.ui.character

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.characterapp.CharacterTopAppBar
import com.example.characterapp.R
import com.example.characterapp.helpers.calculateMod
import com.example.characterapp.helpers.characterBackgrounds
import com.example.characterapp.helpers.characterClasses
import com.example.characterapp.helpers.characterRaces
import com.example.characterapp.helpers.validateACInput
import com.example.characterapp.helpers.validateAbilityScoreInput
import com.example.characterapp.helpers.validateLevelInput
import com.example.characterapp.ui.AppViewModelProvider
import com.example.characterapp.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object CharacterAddDestination : NavigationDestination {
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

    var cancelCreationConfirmation by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CharacterTopAppBar(
                title = stringResource(CharacterAddDestination.titleRes),
                canNavigateBack = canNavigateBack,
                canDelete = false,
                canUpdate = false,
                navigateUp = { cancelCreationConfirmation = true }
            )
        }
    )
    { innerPadding ->
        CharacterEntryBody(
            characterUiState = characterViewModel.characterUiState,
            onCharacterValueChange = characterViewModel::updateUiState,
            onCharacterHPValueChange = characterViewModel::updateUiStateAndHP,
            onSaveClick = {
                coroutineScope.launch {
                    characterViewModel.saveCharacter()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            raceOptions = raceOptions, bgOptions = bgOptions, classOptions = classOptions
        )

        if (cancelCreationConfirmation) {
            AlertDialog(
                onDismissRequest = { cancelCreationConfirmation = false },
                title = { Text(text = stringResource(R.string.cancel_creation_alert_title)) },
                text = {
                    Text(
                        text = stringResource(R.string.cancel_creation_alert_desc),
                        textAlign = TextAlign.Center

                    )
                },
                dismissButton = {
                    TextButton(onClick = { cancelCreationConfirmation = false }) {
                        Text(text = stringResource(R.string.alert_go_back))
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        cancelCreationConfirmation = false
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

@Composable
fun CharacterEntryBody(
    characterUiState: CharacterUiState,
    onCharacterValueChange: (CharacterDetails) -> Unit,
    onCharacterHPValueChange: (CharacterDetails) -> Unit = {},
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    raceOptions: List<String>,
    bgOptions: List<String>,
    classOptions: List<String>,
    isUpdate: Boolean = false
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
        modifier = Modifier.padding(top = 68.dp, start = 16.dp, end = 16.dp),
    ) {
        item {
            CharacterInputForm(
                characterDetails = characterUiState.characterDetails,
                onValueChange = onCharacterValueChange,
                onHPValueChange = onCharacterHPValueChange,
                modifier = Modifier.fillMaxWidth(),
                raceOptions = raceOptions, bgOptions = bgOptions, classOptions = classOptions
            )
        }
        item {
            Button(
                onClick = onSaveClick,
                enabled = characterUiState.isEntryValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.dnd_red))      //https://stackoverflow.com/questions/64376333/background-color-on-button-in-jetpack-compose
            ) {
                if (isUpdate) {
                    Text(text = stringResource(R.string.update_button))
                } else {
                    Text(text = stringResource(R.string.save_button))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterInputForm(
    characterDetails: CharacterDetails,
    modifier: Modifier = Modifier,
    onValueChange: (CharacterDetails) -> Unit = {},
    onHPValueChange: (CharacterDetails) -> Unit = {},
    raceOptions: List<String>,
    bgOptions: List<String>,
    classOptions: List<String>,
    enabled: Boolean = true
) {
    var raceExpanded by remember { mutableStateOf(false) }   //state of dropdown menu (if it is open or not)
    var bgExpanded by remember { mutableStateOf(false) }
    var classExpanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.faint_red))
        ) {
            Column(
                modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Row {
                    Text(
                        text = stringResource(R.string.character_details_title),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                //Name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.chara_name_label),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .align(Alignment.CenterVertically),
                    )
                    Spacer(Modifier.weight(0.25f))
                    OutlinedTextField(      //https://alexzh.com/jetpack-compose-dropdownmenu/
                        value = characterDetails.name,
                        onValueChange = { onValueChange(characterDetails.copy(name = it)) },
                        label = { Text(stringResource(R.string.chara_name_label)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        enabled = enabled,
                        singleLine = true
                    )
                }


                //Race
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.chara_race_dropdown_label),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier
                        )
                        //https://alexzh.com/jetpack-compose-dropdownmenu/
                        ExposedDropdownMenuBox(
                            expanded = raceExpanded,
                            onExpandedChange = {
                                raceExpanded = !raceExpanded // flip state of menu open/closed
                            },
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .menuAnchor(), // defines where the dropdown menu should appear - https://semicolonspace.com/jetpack-compose-dropdown-menu-material3/
                                readOnly = true,
                                value = characterDetails.race,
                                onValueChange = {},
                                label = {
                                    Text(
                                        stringResource(R.string.chara_race_dropdown_label),
                                        fontSize = 11.sp
                                    )
                                },     //https://stackoverflow.com/questions/72901072/how-to-change-the-input-font-size-in-textfield-of-android-jetpack-compose
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = raceExpanded) },
                                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
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
                                            onValueChange(characterDetails.copy(race = option))
                                            raceExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.weight(0.25f))
                    Column(modifier = modifier.weight(1f), horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(R.string.chara_bg_dropdown_label),
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.labelMedium
                        )
                        //background
                        ExposedDropdownMenuBox(
                            expanded = bgExpanded,
                            onExpandedChange = {
                                bgExpanded = !bgExpanded
                            },
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .menuAnchor(),
                                readOnly = true,
                                value = characterDetails.background,
                                onValueChange = {},
                                label = {
                                    Text(
                                        stringResource(R.string.chara_bg_dropdown_label),
                                        fontSize = 11.sp
                                    )
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bgExpanded) },
                                textStyle = TextStyle.Default.copy(fontSize = 15.sp)
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
                                            onValueChange(characterDetails.copy(background = option))
                                            bgExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.chara_class_dropdown_label),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier
                        )
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
                                    classExpanded =
                                        false        //when menu is dismissed, close menu
                                },
                            ) {
                                classOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            onHPValueChange(characterDetails.copy(battleClass = option))
                                            classExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.weight(0.25f))
                    Column(modifier = modifier.weight(0.75f)) {
                        Text(
                            text = stringResource(R.string.chara_level_label),
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.labelMedium
                        )
                        //Level
                        OutlinedTextField(
                            value = characterDetails.level,
                            onValueChange = { onHPValueChange(characterDetails.copy(level = it)) },
                            label = { Text(stringResource(R.string.chara_level_label)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            enabled = enabled,
                            singleLine = true,
                            isError = !validateLevelInput(characterDetails.level),
                            supportingText = {                                                  //https://stackoverflow.com/questions/68573228/how-to-show-error-message-in-outlinedtextfield-in-jetpack-compose
                                if (!validateLevelInput(characterDetails.level)) {
                                    Text(
                                        text = stringResource(R.string.chara_level_error),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                } else {
                                    Text(text = stringResource(R.string.chara_level_desc))
                                }
                            },
                            trailingIcon = {
                                if (!validateLevelInput(characterDetails.level)) {
                                    Icon(
                                        imageVector = Icons.Filled.Warning,         //for some reason, the Error icon does not exist
                                        contentDescription = "Error",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.faint_red))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Row {
                    Text(
                        text = stringResource(R.string.character_stats_title),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                //HP and AC

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(modifier = Modifier.width(105.dp)) {
                        Text(
                            text = stringResource(R.string.chara_hp_label),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        if (validateLevelInput(characterDetails.level) && validateAbilityScoreInput(
                                characterDetails.con
                            ) && characterDetails.battleClass != ""
                        ) {
                            Text(
                                text = characterDetails.maxHP,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.chara_hp_na_label),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Column(modifier = Modifier.width(105.dp)) {
                        OutlinedTextField(
                            value = characterDetails.ac,
                            onValueChange = { onValueChange(characterDetails.copy(ac = it)) },
                            label = { Text(stringResource(R.string.chara_ac_label)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = enabled,
                            singleLine = true,
                            isError = !validateACInput(characterDetails.ac),
                            trailingIcon = {
                                if (!validateACInput(characterDetails.ac)) {
                                    Icon(
                                        imageVector = Icons.Filled.Warning,
                                        contentDescription = "Error",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            modifier = Modifier.width(105.dp)
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.ability_scores_title),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.ability_scores_desc),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )

                //Ability scores (STR, DEX, CON)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(modifier = Modifier.width(105.dp)) {     //https://stackoverflow.com/questions/67681416/jetpack-compose-decrease-height-of-textfield
                        //STR
                        OutlinedTextField(
                            value = characterDetails.str,
                            onValueChange = { onValueChange(characterDetails.copy(str = it)) },
                            label = { Text(stringResource(R.string.chara_str_label)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = enabled,
                            singleLine = true,
                            isError = !validateAbilityScoreInput(characterDetails.str),
                            trailingIcon = {
                                if (!validateAbilityScoreInput(characterDetails.str)) {
                                    Icon(
                                        imageVector = Icons.Filled.Warning,
                                        contentDescription = "Error",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },

                            )
                        Text(
                            text = stringResource(R.string.ability_mod_label),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = calculateMod(characterDetails.str),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(modifier = Modifier.width(105.dp)) {
                        //DEX
                        OutlinedTextField(
                            value = characterDetails.dex,
                            onValueChange = { onValueChange(characterDetails.copy(dex = it)) },
                            label = { Text(stringResource(R.string.chara_dex_label)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = enabled,
                            singleLine = true,
                            isError = !validateAbilityScoreInput(characterDetails.dex),
                            trailingIcon = {
                                if (!validateAbilityScoreInput(characterDetails.dex)) {
                                    Icon(
                                        imageVector = Icons.Filled.Warning,
                                        contentDescription = "Error",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            modifier = Modifier.width(105.dp)
                        )
                        Text(
                            text = stringResource(R.string.ability_mod_label),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = calculateMod(characterDetails.dex),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(modifier = Modifier.width(105.dp)) {

                        //CON
                        OutlinedTextField(
                            value = characterDetails.con,
                            onValueChange = { onHPValueChange(characterDetails.copy(con = it)) },
                            label = { Text(stringResource(R.string.chara_con_label)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = enabled,
                            singleLine = true,
                            isError = !validateAbilityScoreInput(characterDetails.con),
                            trailingIcon = {
                                if (!validateAbilityScoreInput(characterDetails.con)) {
                                    Icon(
                                        imageVector = Icons.Filled.Warning,
                                        contentDescription = "Error",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            modifier = Modifier.width(105.dp)
                        )
                        Text(
                            text = stringResource(R.string.ability_mod_label),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = calculateMod(characterDetails.con),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }


                //Ability scores (INT, WIS, CHA)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(modifier = Modifier.width(105.dp)) {     //https://stackoverflow.com/questions/67681416/jetpack-compose-decrease-height-of-textfield
                        //INT
                        OutlinedTextField(
                            value = characterDetails.int,
                            onValueChange = { onValueChange(characterDetails.copy(int = it)) },
                            label = { Text(stringResource(R.string.chara_int_label)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = enabled,
                            singleLine = true,
                            isError = !validateAbilityScoreInput(characterDetails.int),
                            trailingIcon = {
                                if (!validateAbilityScoreInput(characterDetails.int)) {
                                    Icon(
                                        imageVector = Icons.Filled.Warning,
                                        contentDescription = "Error",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },

                            )
                        Text(
                            text = stringResource(R.string.ability_mod_label),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = calculateMod(characterDetails.int),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(modifier = Modifier.width(105.dp)) {
                        //WIS
                        OutlinedTextField(
                            value = characterDetails.wis,
                            onValueChange = { onValueChange(characterDetails.copy(wis = it)) },
                            label = { Text(stringResource(R.string.chara_wis_label)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = enabled,
                            singleLine = true,
                            isError = !validateAbilityScoreInput(characterDetails.wis),
                            trailingIcon = {
                                if (!validateAbilityScoreInput(characterDetails.wis)) {
                                    Icon(
                                        imageVector = Icons.Filled.Warning,
                                        contentDescription = "Error",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            modifier = Modifier.width(105.dp)
                        )
                        Text(
                            text = stringResource(R.string.ability_mod_label),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = calculateMod(characterDetails.wis),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(modifier = Modifier.width(105.dp)) {

                        //CHA
                        OutlinedTextField(
                            value = characterDetails.cha,
                            onValueChange = { onValueChange(characterDetails.copy(cha = it)) },
                            label = { Text(stringResource(R.string.chara_cha_label)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = enabled,
                            singleLine = true,
                            isError = !validateAbilityScoreInput(characterDetails.cha),
                            trailingIcon = {
                                if (!validateAbilityScoreInput(characterDetails.cha)) {
                                    Icon(
                                        imageVector = Icons.Filled.Warning,
                                        contentDescription = "Error",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            modifier = Modifier.width(105.dp)
                        )
                        Text(
                            text = stringResource(R.string.ability_mod_label),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = calculateMod(characterDetails.cha),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterAddScreenPreview() {
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
    CharacterEntryBody(
        characterUiState = CharacterUiState(
            CharacterDetails(
                name = "Cyn",
                race = "Half-Elf",
                battleClass = "Ranger",
                level = "3",
                str = "9",
                dex = "16",
                con = "12",
                int = "8",
                wis = "14",
                cha = "11",
                maxHP = "25",
                ac = "13",
                background = "Criminal"
            )
        ),
        onCharacterValueChange = {},
        onSaveClick = { },
        raceOptions = raceOptions,
        bgOptions = bgOptions,
        classOptions = classOptions
    )
}