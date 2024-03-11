package com.david.movie.lab.ui.screens.discover

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.material3.SearchBar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.david.movie.lab.UiState
import com.david.movie.lab.main.Destinations
import com.david.movie.lab.repo.model.MovieItem
import com.david.movie.lab.ui.composable.ActionButton
import com.david.movie.lab.ui.composable.AppSpacer
import com.david.movie.lab.ui.composable.ChipsGrid
import com.david.movie.lab.ui.composable.ChipsModel
import com.david.movie.lab.ui.composable.MovieCard
import com.david.movie.lab.ui.screens.ErrorScreen
import com.david.movie.lab.ui.screens.LoadingScreen
import com.david.movie.notwork.dto.Genres
import androidx.compose.ui.platform.LocalSoftwareKeyboardController.current as current1


@Composable
fun DiscoverScreen(
    navController: NavController,
    viewModel: DiscoverViewModel,
    innerPadding : PaddingValues,

    )  {
    val uiState by viewModel.genresState.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()

    val discoveredMovies = viewModel.discoveredMovies.collectAsState()



    if (discoveredMovies.value is UiState.Success && (discoveredMovies.value as UiState.Success).data.isNotEmpty()) {
        BackHandler(enabled = true) {
            viewModel.handleBackWithNonEmptyCategories()

        }

    }

    if (discoveredMovies.value is UiState.Success && (discoveredMovies.value as UiState.Success).data.isNotEmpty()) {
        val movies = (discoveredMovies.value as UiState.Success).data
        BuildMovieList(innerPadding, movies, navController , selectedGenre)
    } else {

        when (val state = uiState) {
            is UiState.Loading -> LoadingScreen()
            is UiState.Success -> GenresView(
                popularPersons = state.data!!,
                selectedGenre = selectedGenre,
                navController = navController,
                innerPadding = innerPadding,
                viewModel = viewModel
            )

            is UiState.Error -> ErrorScreen(state.exception.localizedMessage ?: "An error occurred")
        }

    }


}

@Composable
fun BuildMovieList(
    innerPadding: PaddingValues,
    movies: List<MovieItem>,
    navController: NavController,
    selectedGenre: List<Int>
) {
    val gridState = rememberLazyGridState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 180.dp),
                modifier = Modifier
                    .fillMaxSize(),
                state = gridState
            ) {
                item { AppSpacer(height = 40.dp)}
                item { AppSpacer(height = 40.dp)}
                items(movies) { movie ->
                    MovieCard(movie = movie, onMovieClick = { movieItem ->
                        navController.navigate(Destinations.movieDetailsRoute(movieId =  movieItem.id.toString()))
                    })
                }

            }
        }
    }

}

@Composable
fun GenresView(
    popularPersons: Genres,
    selectedGenre : List<Int>,
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: DiscoverViewModel
) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(innerPadding)
        ) {
        AppSpacer(height = 30.dp)
        EnhancedSearchBar(
            query = "",
            onQueryChanged = {},
            onSearch = {},
            viewModel = viewModel
        )
        AppSpacer(height = 30.dp)
        Text(text = "Discover movies by genres" , modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
        ChipsGrid(popularPersons.genres.map { ChipsModel(
            title = it.name,
            onClick = {
                viewModel.onSelectedGenre(it)
            },
            isSelected = selectedGenre.contains(it.id)
        ) })
        AppSpacer(height = 30.dp)
            ActionButton(
                modifier = Modifier.padding(19.dp),
                text = "Discover",
                backgroundColor = Color.Blue.copy(alpha = 0.7f),
                onClick = {
                    viewModel.onDiscoverClicked()
                })
    }
}


@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    // A row that contains the text field and search icon
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Use weight to allow the TextField to fill the space
            placeholder = {
                Text(text = "Search")
            },
            singleLine = true, // Make TextField single line
            trailingIcon = {
                if (query.isNotEmpty()) {
                    // Clear text icon
                    IconButton(onClick = { onQueryChanged("") }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear text")
                    }
                } else {
                    // Search icon
                    IconButton(onClick = { onSearch(query) }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(query)
                }
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedSearchBar(
    viewModel: DiscoverViewModel,
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit
) {

    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val countriesList by viewModel.countriesList.collectAsState()

    SearchBar(
        query =  searchText,//searchText,//text showed on SearchBar
        onQueryChange = viewModel::onSearchTextChange, //update the value of searchText
        onSearch = viewModel::onSearchTextChange, //the callback to be invoked when the input service triggers the ImeAction.Search action
        active = isSearching, //whether the user is searching or not
        onActiveChange = { viewModel.onToogleSearch() }, //the callback to be invoked when this search bar's active state is changed
        modifier = Modifier
            .fillMaxWidth()
            .padding(19.dp)
    ) {
        LazyColumn {
            items(countriesList) { country ->
                Text(
                    text = country,
                    modifier = Modifier.padding(
                        start = 8.dp,
                        top = 4.dp,
                        end = 8.dp,
                        bottom = 4.dp)
                )
            }
        }
    }
}



