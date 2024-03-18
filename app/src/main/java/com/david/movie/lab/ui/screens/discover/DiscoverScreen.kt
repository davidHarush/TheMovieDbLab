package com.david.movie.lab.ui.screens.discover

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.david.movie.lab.UiState
import com.david.movie.lab.main.AppRoutes
import com.david.movie.lab.repo.model.MovieItem
import com.david.movie.lab.ui.composable.ActionButton
import com.david.movie.lab.ui.composable.AppSpacer
import com.david.movie.lab.ui.composable.ChipsGrid
import com.david.movie.lab.ui.composable.ChipsModel
import com.david.movie.lab.ui.composable.MovieCard
import com.david.movie.lab.ui.screens.ErrorScreen
import com.david.movie.lab.ui.screens.LoadingScreen
import com.david.movie.notwork.dto.Genres


@Composable
fun DiscoverScreen(
    navController: NavController,
    viewModel: DiscoverViewModel,
    innerPadding: PaddingValues,

    ) {
    val uiState by viewModel.genresState.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()

    val discoveredMovies = viewModel.discoveredMovies.collectAsState()



    if (discoveredMovies.value is UiState.Success && (discoveredMovies.value as UiState.Success).data.isNotEmpty()) {
        BackHandler(enabled = true) {
            viewModel.handleBackWithNonEmptyCategories()
        }
    }

    if (discoveredMovies.value is UiState.Success && (discoveredMovies.value as UiState.Success).data.isNotEmpty()) {
        /** discovered movies are available */
        val movies = (discoveredMovies.value as UiState.Success).data
        BuildMovieList(viewModel, innerPadding, movies, navController, selectedGenre)
    } else {
        /** show discover screen  */
        when (val state = uiState) {
            is UiState.Loading -> LoadingScreen()
            is UiState.Success -> DiscoverView(
                genres = state.data,
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
    viewModel: DiscoverViewModel,
    innerPadding: PaddingValues,
    movies: List<MovieItem>,
    navController: NavController,
    selectedGenre: List<Int>
) {
    val gridState = rememberLazyGridState()

    if (movies.size == 1) {
        navController.navigate(AppRoutes.movieDetailsRoute(movieId = movies[0].id.toString()))
        viewModel.cleanSearchResult()
        return
    }

    val title = if (selectedGenre.isNotEmpty()) {
        "Discover Movies by ${selectedGenre.map { viewModel.getGenreName(it) }}"
    } else {
        "Discover Movies"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {


            // Text(text = title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 180.dp),
                modifier = Modifier
                    .fillMaxSize(),
                state = gridState
            ) {
                item { AppSpacer(height = 40.dp) }
                item { AppSpacer(height = 40.dp) }
                items(movies) { movie ->
                    MovieCard(movie = movie, onMovieClick = { movieItem ->
                        navController.navigate(AppRoutes.movieDetailsRoute(movieId = movieItem.id.toString()))
                    })
                }

            }
        }
    }

}


/**
 * DiscoverView
 * show genres view and search bar
 */
@Composable
fun DiscoverView(
    genres: Genres,
    selectedGenre: List<Int>,
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: DiscoverViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AppSpacer(height = 16.dp)
        SearchBar(
            viewModel = viewModel
        )
        HorizontalDivider(
            color = Color.Cyan.copy(0.5f),
            thickness = 2.dp,
            modifier = Modifier.padding(16.dp),
        )
        Text(
            text = "Discover movies by genres",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        ChipsGrid(genres.genres.map {
            ChipsModel(
                title = it.name,
                onClick = {
                    viewModel.onSelectedGenre(it)
                },
                isSelected = selectedGenre.contains(it.id)
            )
        })
        AppSpacer(height = 16.dp)
        ActionButton(
            icon = Icons.Filled.Search,
            modifier = Modifier.padding(horizontal = 19.dp),
            text = "Let's find Movies!",
            backgroundColor = Color.Cyan.copy(alpha = 0.6f),
            onClick = {
                viewModel.onDiscoverClicked()
            })

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    viewModel: DiscoverViewModel,
) {

    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val moviesList by viewModel.searchMoviesPreview.collectAsState()


    SearchBar(
        colors = SearchBarDefaults.colors(
            containerColor = if (isSearching) MaterialTheme.colorScheme.secondaryContainer.copy(
                alpha = 0.7f
            ) else MaterialTheme.colorScheme.secondaryContainer,
            dividerColor = Color.Cyan.copy(alpha = 0.7f),
            inputFieldColors = SearchBarDefaults.inputFieldColors(),
        ),

        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search")
        },

        placeholder = { Text("Search Movies") },
        query = searchText,
        onQueryChange = viewModel::onPreviewText,
        onSearch = viewModel::onSearchText,
        active = isSearching,
        onActiveChange = { viewModel.onToggleSearch() },

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (moviesList is UiState.Success && (moviesList as UiState.Success).data.isNotEmpty()) {
            val movies = (moviesList as UiState.Success).data
            LazyColumn {
                items(movies) { movieTitle ->
                    Text(
                        text = movieTitle,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { viewModel.onSearchText(movieTitle) },
                    )
                }
            }
        }
    }
}


