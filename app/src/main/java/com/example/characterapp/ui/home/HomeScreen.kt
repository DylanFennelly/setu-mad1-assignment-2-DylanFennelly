package com.example.characterapp.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.characterapp.CharacterTopAppBar
import com.example.characterapp.R
import com.example.characterapp.data.CharacterModel
import com.example.characterapp.ui.AppViewModelProvider
import com.example.characterapp.ui.navigation.NavigationDestination

object HomeDestination: NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_title

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToCreateCharacter: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    characterViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
             CharacterTopAppBar(
                 title = stringResource(HomeDestination.titleRes),
                 canNavigateBack = false,
                 scrollBehavior = scrollBehavior
             )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCreateCharacter
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            characterList = characterViewModel.characters.value,
            onItemClick = navigateToItemUpdate,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
fun HomeBody(
    characterList: List<CharacterModel>, onItemClick: (Int) -> Unit, modifier: Modifier = Modifier
){
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
            CharacterList(characters = characterList, onTodoClick = { })
        }
    }
}

@Composable
fun CharacterList(characters: List<CharacterModel>, onTodoClick: (CharacterModel) -> Unit) {
    LazyColumn {
        items(characters) { character ->
            CharacterItemRow(character = character, onTodoClick = { onTodoClick(character) })
        }
    }
}

@Composable
fun CharacterItemRow(character: CharacterModel, onTodoClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onTodoClick() }
    ) {
        Text(text = character.name, modifier = Modifier.weight(1f))
        Checkbox(checked = false , onCheckedChange = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterInput(onAddTodo: (String) -> Unit) {
    var task by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextField(
            value = task,
            onValueChange = { task = it },
            modifier = Modifier.weight(1f),
            label = { Text("Enter character") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onAddTodo(task) }) {
            Text("Add")
        }
    }
}
