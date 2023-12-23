package com.example.characterapp.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.characterapp.CharacterTopAppBar
import com.example.characterapp.R
import com.example.characterapp.data.CharacterModel
import com.example.characterapp.helpers.characterDisplayNameTruncate
import com.example.characterapp.ui.AppViewModelProvider
import com.example.characterapp.ui.navigation.NavigationDestination
import com.example.characterapp.ui.theme.CharacterAppTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_title

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToCreateCharacter: () -> Unit,
    navigateToCharacterDetails: (Long) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by homeViewModel.homeUiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            CharacterTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                canDelete = false,
                canUpdate = false,
                scrollBehavior = scrollBehavior,
                modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCreateCharacter,
                containerColor = colorResource(R.color.dnd_red),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            characterList = homeUiState.characterList,
            onItemClick = navigateToCharacterDetails,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }
}

@Composable
fun HomeBody(
    characterList: List<CharacterModel>, onItemClick: (Long) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 76.dp, start = 16.dp, end = 16.dp),  //Generative AI Usage 1.
        horizontalAlignment = Alignment.CenterHorizontally      //https://stackoverflow.com/questions/59713224/jetpack-compose-column-gravity-center
    ) {
        if (characterList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_character_desc),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            CharacterList(characters = characterList, onCharacterClick = { onItemClick(it.id) })
        }
    }
}

@Composable
fun CharacterList(characters: List<CharacterModel>, onCharacterClick: (CharacterModel) -> Unit) {
    LazyColumn {
        items(characters) { character ->
            CharacterItem(character = character,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onCharacterClick(character) })
        }
    }
}

@Composable
fun CharacterItem(character: CharacterModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.faint_red))
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            ) {
                Text(
                    text = characterDisplayNameTruncate(character.name, 72),
                    modifier = Modifier.weight(1f),
                    style = characterNameFontSize(name = character.name),
                )
                Text(
                    text = "Level ${character.level} ${character.battleClass}",
                    modifier = Modifier.align(Alignment.CenterVertically),      //https://stackoverflow.com/questions/70708107/how-to-make-text-centered-vertically-in-android-compose
                    style = MaterialTheme.typography.titleMedium,

                    )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            ) {
                Text(
                    text = character.race,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = "Max HP: ${character.maxHP}",
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}

//changes size of character font name depending on length of name
@Composable
fun characterNameFontSize(name: String): androidx.compose.ui.text.TextStyle {
    return if (name.length < 18) {
        MaterialTheme.typography.titleLarge
    } else if (name.length < 45) {
        MaterialTheme.typography.titleMedium
    } else {      //if name length is greater than 45 characters
        MaterialTheme.typography.titleSmall
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterItemPreview() {
    CharacterAppTheme {
        CharacterItem(
            CharacterModel(
                name = "Cyn",
                race = "Half-Elf",
                battleClass = "Ranger",
                level = 3,
                str = 9,
                dex = 16,
                con = 12,
                int = 8,
                wis = 14,
                cha = 11,
                maxHP = 25,
                ac = 13,
                background = "Criminal"
            )
        )
    }
}

