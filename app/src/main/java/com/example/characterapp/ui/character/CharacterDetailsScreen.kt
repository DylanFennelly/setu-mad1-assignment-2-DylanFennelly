package com.example.characterapp.ui.character

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.characterapp.CharacterTopAppBar
import com.example.characterapp.R
import com.example.characterapp.data.CharacterModel
import com.example.characterapp.helpers.calculateMod
import com.example.characterapp.ui.AppViewModelProvider
import com.example.characterapp.ui.navigation.NavigationDestination
import com.example.characterapp.ui.theme.CharacterAppTheme

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
    canDelete: Boolean = true,
    modifier: Modifier = Modifier,
    detailsViewModel: CharacterDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = detailsViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

   Scaffold(
       topBar = {
           CharacterTopAppBar(
               title = uiState.value.characterDetails.name,
               canNavigateBack = canNavigateBack,
               canDelete = canDelete,
               navigateUp = onNavigateUp
           )
       }
   ) {innerPadding ->
        CharacterDetailsBody(
            characterDetailsUiState = uiState.value,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
   }
}

@Composable
private fun CharacterDetailsBody(
    characterDetailsUiState: CharacterDetailsUiState,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        CharacterDetailsInfo(
            characterModel = characterDetailsUiState.characterDetails.toCharacter(),
            modifier = Modifier.fillMaxWidth()
        )
    }

}

@Composable
fun CharacterDetailsInfo(
    characterModel: CharacterModel, modifier: Modifier = Modifier
){
    //Character Details (name, race, background, class, level)
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.faint_red))
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
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
                Spacer(Modifier.weight(1f))
                Text(
                    text = characterModel.name,
                    fontWeight= FontWeight.Bold,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column() {
                    Text(
                        text = stringResource(R.string.chara_race_dropdown_label),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                    )
                    Text(
                        text = characterModel.race,
                        fontWeight= FontWeight.Bold,
                    )
                }
                Spacer(Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.End
                ){
                    Text(
                        text = stringResource(R.string.chara_bg_dropdown_label),
                        textAlign = TextAlign.Right,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = characterModel.background,
                        fontWeight= FontWeight.Bold,
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column() {
                    Text(
                        text = stringResource(R.string.chara_class_dropdown_label),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                    )
                    Text(
                        text = characterModel.battleClass,
                        fontWeight= FontWeight.Bold,
                    )
                }
                Spacer(Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.End
                ){
                    Text(
                        text = stringResource(R.string.chara_level_label),
                        textAlign = TextAlign.Right,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = characterModel.level.toString(),
                        fontWeight= FontWeight.Bold,
                    )
                }
            }
        }
    }

    //Character Stats (ability scores, HP, AC)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(modifier = Modifier.width(105.dp)) {
                    Text(
                        text = stringResource(R.string.chara_hp_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = characterModel.maxHP.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )
                }
                Column(modifier = Modifier.width(105.dp)) {
                    Text(
                        text = stringResource(R.string.chara_ac_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = characterModel.ac.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(modifier = Modifier
                    .width(95.dp)
                    .background(color = colorResource(R.color.background_red), shape = RoundedCornerShape((20.dp)))     //https://codingwithrashid.com/how-to-create-column-with-rounded-corners-in-jetpack-compose/
                    ) {     //https://stackoverflow.com/questions/67681416/jetpack-compose-decrease-height-of-textfield
                    //STR
                    Text(
                        text = stringResource(R.string.chara_str_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    Text(
                        text = characterModel.str.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )
                    Text(
                        text = stringResource(R.string.ability_mod_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = calculateMod(characterModel.str.toString()),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .width(95.dp)
                        .background(color = colorResource(R.color.background_red), shape = RoundedCornerShape((20.dp)))
                ) {
                    //DEX
                    Text(
                        text = stringResource(R.string.chara_dex_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    Text(
                        text = characterModel.dex.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )
                    Text(
                        text = stringResource(R.string.ability_mod_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = calculateMod(characterModel.dex.toString()),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .width(95.dp)
                        .background(color = colorResource(R.color.background_red), shape = RoundedCornerShape((20.dp)))
                ) {
                    //CON
                    Text(
                        text = stringResource(R.string.chara_con_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    Text(
                        text = characterModel.con.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )
                    Text(
                        text = stringResource(R.string.ability_mod_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = calculateMod(characterModel.con.toString()),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .width(95.dp)
                        .background(color = colorResource(R.color.background_red), shape = RoundedCornerShape((20.dp)))
                ) {
                    //INT
                    Text(
                        text = stringResource(R.string.chara_int_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    Text(
                        text = characterModel.int.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )
                    Text(
                        text = stringResource(R.string.ability_mod_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = calculateMod(characterModel.int.toString()),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .width(95.dp)
                        .background(color = colorResource(R.color.background_red), shape = RoundedCornerShape((20.dp)))
                ) {
                    //WIS
                    Text(
                        text = stringResource(R.string.chara_wis_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    Text(
                        text = characterModel.wis.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )
                    Text(
                        text = stringResource(R.string.ability_mod_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = calculateMod(characterModel.wis.toString()),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .width(95.dp)
                        .background(color = colorResource(R.color.background_red), shape = RoundedCornerShape((20.dp)))
                ) {
                    //CHA
                    Text(
                        text = stringResource(R.string.chara_cha_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    Text(
                        text = characterModel.cha.toString(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )
                    Text(
                        text = stringResource(R.string.ability_mod_label),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = calculateMod(characterModel.cha.toString()),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsBodyPreview() {
    CharacterAppTheme {
        CharacterDetailsBody(
            characterDetailsUiState = CharacterDetailsUiState(
                characterDetails = CharacterDetails(
                    name = "Cyn",
                    race = "Elf",
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
                    background = "Guild Artisan"
                )
            )
        )
    }
}