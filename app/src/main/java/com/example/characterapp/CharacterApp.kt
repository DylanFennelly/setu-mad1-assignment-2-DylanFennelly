package com.example.characterapp

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.characterapp.ui.navigation.CharacterNavHost

/**
 * Top level composable that represents screens for the application.
 */
@Composable
fun CharacterApp(navController: NavHostController = rememberNavController()) {
    CharacterNavHost(navController = navController)
}

/**
 * App bar to display title and conditionally display the back navigation.
 */
@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    canDelete: Boolean,         //used to display delete icon
    canUpdate: Boolean,         //used to display update icon
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onUpdateClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                        tint = Color.White      //Generative AI Usage 2.
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            //https://developer.android.com/jetpack/compose/components/app-bars
            containerColor = Color(android.graphics.Color.parseColor(stringResource(R.color.dnd_red))),   //https://developermemos.com/posts/using-hex-colors-compose
            titleContentColor = Color.White
        ),
        actions = {
            if (canDelete) {
                IconButton(onClick = { onDeleteClick() }) {     //Generative AI Usage 3.
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.delete_button),
                        tint = Color.White
                    )
                }
            }
            if (canUpdate) {
                IconButton(onClick = { onUpdateClick() }) {     //Generative AI Usage 3.
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.update_button),
                        tint = Color.White
                    )
                }
            }

        }
    )
}