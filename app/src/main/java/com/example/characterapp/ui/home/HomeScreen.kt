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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.characterapp.R
import com.example.characterapp.data.CharacterModel
import com.example.characterapp.ui.navigation.NavigationDestination

object HomeDestination: NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToCreateCharacter: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    characterViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val todos = characterViewModel.todos;

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCreateCharacter
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CharacterList(todos = todos.value, onTodoClick = characterViewModel::toggleTodoStatus)
            CharacterInput(onAddTodo = characterViewModel::addCharacter)
        }
    }


}

@Composable
fun CharacterList(todos: List<CharacterModel>, onTodoClick: (CharacterModel) -> Unit) {
    LazyColumn {
        items(todos) { todo ->
            CharacterItemRow(todo = todo, onTodoClick = { onTodoClick(todo) })
        }
    }
}

@Composable
fun CharacterItemRow(todo: CharacterModel, onTodoClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onTodoClick() }
    ) {
        Text(text = todo.task, modifier = Modifier.weight(1f))
        Checkbox(checked = todo.isCompleted, onCheckedChange = null)
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
