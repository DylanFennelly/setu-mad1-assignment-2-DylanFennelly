# Generative AI Usage Declaration

Generative AI was used to assist with the development of this project. The following is a list of times AI was used, the overview of what the usage achieved, and the exact conversation logs.

_____

## Generative AI Usage 1.

#### Usage Overview:
In this instance, ChatGPT was used to solve an issue with the TopAppBar overlapping the "No characters created" text.

#### Chat log:
```
Me:
I am developing an Android Application using Jetpack Compose. I added a TopAppBar to the app. However, it overlays and overlaps the Text in the HomeBody composable. How do I make it so the HomeBody content displays belwo the TopAppBar?

HomeScreen composable:
"@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToCreateCharacter: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    characterViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
             CharacterTopAppBar(
                 title = stringResource(R.string.home_title),
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
        }
    ) { innerPadding ->
        HomeBody(
            characterList = characterViewModel.todos.value,
            onItemClick = navigateToItemUpdate,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}"

HomeBody composable:
"@Composable
fun HomeBody(
    characterList: List<CharacterModel>, onItemClick: (Int) -> Unit, modifier: Modifier = Modifier
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally      //https://stackoverflow.com/questions/59713224/jetpack-compose-column-gravity-center
    ) {
        if (characterList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_character_desc),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            CharacterList(todos = characterList, onTodoClick = { })
        }
    }
}"

CharacterTopAppBar composable:
"@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
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
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}"


ChatGPT:
It appears you are using Jetpack Compose to build a UI with a Scaffold that contains a TopAppBar and a FloatingActionButton. The issue you're facing is that the content in HomeBody is being overlapped by the TopAppBar. To resolve this, you can use the contentPadding parameter of Scaffold to provide additional padding for the content area, ensuring that it doesn't overlap with the TopAppBar.

Here's an updated version of your Scaffold in the HomeScreen composable:

kotlin

Scaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
        CharacterTopAppBar(
            title = stringResource(R.string.home_title),
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
    content = { innerPadding ->
        HomeBody(
            characterList = characterViewModel.todos.value,
            onItemClick = navigateToItemUpdate,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    },
    contentPadding = rememberInsetsPaddingValues(
        LocalWindowInsets.current.statusBars,
        applyStart = true,
        applyTop = true,
        applyEnd = true,
        applyBottom = false
    )
)

In this example, contentPadding is used to apply padding for system bars (status bar), and you can adjust it based on your requirements. This should ensure that the content in HomeBody is displayed below the TopAppBar without being overlapped.


Me:
contentPadding is not a valid variable for the Scaffold composable


ChatGPT:
I apologize for the confusion. It looks like I made an error in my response. In Jetpack Compose, there isn't a direct contentPadding parameter for the Scaffold composable.

To achieve the desired effect, you can use the padding modifier inside the Column in your HomeBody composable to ensure that the content doesn't overlap with the TopAppBar. Here's how you can modify the HomeBody composable:

kotlin

@Composable
fun HomeBody(
    characterList: List<CharacterModel>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = /* Adjust the top padding based on your needs */ 16.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (characterList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_character_desc),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            CharacterList(todos = characterList, onTodoClick = { })
        }
    }
}

In this example, I added a padding modifier to the Column inside the HomeBody composable. You can adjust the top padding value to ensure that the content is positioned below the TopAppBar.

I appreciate your understanding, and I hope this resolves the issue you were facing.
```

## Generative AI Usage 2.

#### Usage Overview:
In this instance, ChatGPT was used to change the colour of a MaterialUI Icon.

#### Chat log:
```
Me:
The TopAppBar has a back arrow that allows for backwards navigation through the NavController stack. This back arrow is an Icon Button that uses the MaterialUI Icons for the back arrow. Is there any way to change the color of this icon?

"CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            //https://developer.android.com/jetpack/compose/components/app-bars
            containerColor = Color(android.graphics.Color.parseColor(stringResource(R.color.dnd_red))),   //https://developermemos.com/posts/using-hex-colors-compose
            titleContentColor = Color.White
        )"


ChatGPT:
Certainly! To change the color of the back arrow icon in the TopAppBar, you can apply the desired tint to the Icon using the tint parameter. Here's an example of how you can modify your IconButton:

kotlin

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
                    tint = Color.White // Set the desired color here
                )
            }
        }
    },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Color(android.graphics.Color.parseColor(stringResource(R.color.dnd_red))),
        titleContentColor = Color.White
    )
)

In the provided example, I set the tint parameter of the Icon to Color.White. You can replace it with any color you prefer. Additionally, you can use a color resource instead of Color.White if you have defined colors in your resources.

Make sure to adjust the color value in the tint parameter to match your desired color for the back arrow icon.
```

## Generative AI Usage 3.

#### Usage Overview:
In this instance, ChatGPT was used to fix an issue with a function not triggering upon IconButton press.

#### Chat log:
```
Me:
The following is a Composable for an item details page. The TopAppBar has a delete button that upon clicking, should flip a boolean and trigger an alert dialog to appear. However, clickling the iconButton is not changing the boolean.

The composable:
"@Composable
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
    var deleteConfirmation by rememberSaveable { mutableStateOf(false) }

   Scaffold(
       topBar = {
           CharacterTopAppBar(
               title = uiState.value.characterDetails.name,
               canNavigateBack = canNavigateBack,
               canDelete = canDelete,
               navigateUp = onNavigateUp,
               onDeleteClick = {deleteConfirmation = true}
           )
           Text(text = "deleteConfirmation: $deleteConfirmation")
       }
   ) {innerPadding ->
        CharacterDetailsBody(
            characterDetailsUiState = uiState.value,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )

       if(deleteConfirmation){
           AlertDialog(onDismissRequest = { deleteConfirmation = false }, confirmButton = { /*TODO*/ }, text = {Text("Test")})
       }
   }
}"

CharacterTopAppBar:
"@Composable
fun CharacterTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    canDelete: Boolean,         //used to display delete icon
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
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
            if (canDelete){
                IconButton(onClick = { onDeleteClick }) {
                    Icon(imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.delete_button),
                        tint = Color.White
                    )
                }
            }
        }
    )
}"


ChatGPT:
The issue you're facing is in the way you're calling the onDeleteClick lambda in the CharacterTopAppBar. You need to invoke the lambda by using onDeleteClick() instead of { onDeleteClick }.

Here's the corrected part of your code:

kotlin

actions = {
    if (canDelete){
        IconButton(onClick = { onDeleteClick() }) { // Corrected invocation
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.delete_button),
                tint = Color.White
            )
        }
    }
}

By making this change, you ensure that the onDeleteClick lambda is actually invoked when the IconButton is clicked, triggering the desired behavior in the CharacterDetailsScreen.
```