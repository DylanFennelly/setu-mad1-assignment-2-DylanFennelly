package com.example.characterapp.ui.character

import android.annotation.SuppressLint
import androidx.annotation.DimenRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.characterapp.CharacterTopAppBar
import com.example.characterapp.R
import com.example.characterapp.ui.AppViewModelProvider
import com.example.characterapp.ui.home.HomeViewModel
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
        ItemEntryBody(
            characterUiState = characterViewModel.characterUiState,
            onCharacterValueChange = characterViewModel::updateUiState,
            onSaveClick = { /*TODO*/ },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun ItemEntryBody(
    characterUiState: CharacterUiState,
    onCharacterValueChange: (CharacterDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){
  Column (
      verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_large)),
      modifier = Modifier.padding(top = 76.dp, start = 16.dp, end = 16.dp),
  ){
      ItemInputForm(
          characterDetails = characterUiState.characterDetails,
          onValueChange =  onCharacterValueChange,
          modifier = Modifier.fillMaxWidth()
      )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemInputForm(
    characterDetails: CharacterDetails,
    modifier: Modifier = Modifier,
    onValueChange: (CharacterDetails) -> Unit = {},
    enabled: Boolean = true
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ){
        OutlinedTextField(
            value = characterDetails.name,
            onValueChange = { onValueChange(characterDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.chara_name_label)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

    }
}