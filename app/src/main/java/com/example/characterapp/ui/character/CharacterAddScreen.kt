package com.example.characterapp.ui.character

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.characterapp.CharacterTopAppBar
import com.example.characterapp.R
import com.example.characterapp.helpers.calculateHP
import com.example.characterapp.helpers.calculateMod
import com.example.characterapp.helpers.validateACInput
import com.example.characterapp.helpers.validateAbilityScoreInput
import com.example.characterapp.helpers.validateLevelInput
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
            onCharacterHPValueChange = characterViewModel::updateUiStateAndHP,
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
    onCharacterHPValueChange: (CharacterDetails) -> Unit = {},
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
          onHPValueChange = onCharacterHPValueChange,
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
    onHPValueChange: (CharacterDetails) -> Unit = {},
    raceOptions: List<String>,
    bgOptions: List<String>,
    classOptions: List<String>,
    enabled: Boolean = true
){
    var raceExpanded by remember { mutableStateOf(false)}   //state of dropdown menu (if it is open or not)
    var bgExpanded by remember { mutableStateOf(false)}
    var classExpanded by remember { mutableStateOf(false)}

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ){
        item {
            //Name
            OutlinedTextField(      //https://alexzh.com/jetpack-compose-dropdownmenu/
                value = characterDetails.name,
                onValueChange = { onValueChange(characterDetails.copy(name = it)) },
                label = { Text(stringResource(R.string.chara_name_label)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
        }


        //Race
        //https://alexzh.com/jetpack-compose-dropdownmenu/
        item {
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
                                onValueChange(characterDetails.copy(race=option))
                                raceExpanded = false
                            }
                        )
                    }
                }
            }
        }

        //background
        item {
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
                                onValueChange(characterDetails.copy(background=option))
                                bgExpanded = false
                            }
                        )
                    }
                }
            }
        }

        //class
        item {
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
                                onHPValueChange(characterDetails.copy(battleClass = option))
                                classExpanded = false
                            }
                        )
                    }
                }
            }
        }

        //Level
        item {
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
        item {
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
        }

        //Ability scores (STR, DEX, CON)
        item {
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
        }

        //Ability scores (INT, WIS, CHA)
        item {
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

        //HP and AC
        item{
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Column(modifier = Modifier.width(105.dp)) {
                    Text(
                        text = stringResource(R.string.chara_hp_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    if(validateLevelInput(characterDetails.level) && validateAbilityScoreInput(characterDetails.con) && characterDetails.battleClass != "") {
                        Text(
                            text = characterDetails.maxHP.toString(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }else{
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
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterAddScreenPreview(){
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
    CharacterEntryBody(
        characterUiState = CharacterUiState(CharacterDetails(
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
        onSaveClick = { /*TODO*/ },
        raceOptions = raceOptions,
        bgOptions = bgOptions,
        classOptions = classOptions
    )
}