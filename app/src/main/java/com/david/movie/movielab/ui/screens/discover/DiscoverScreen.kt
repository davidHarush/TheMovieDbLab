package com.david.movie.movielab.ui.screens.discover

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.david.movie.movielab.UiState
import com.david.movie.movielab.main.AppRoutes
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.movielab.ui.composable.ActionButton
import com.david.movie.movielab.ui.composable.AppSpacer
import com.david.movie.movielab.ui.composable.AppSwitch
import com.david.movie.movielab.ui.composable.ChipsGrid
import com.david.movie.movielab.ui.composable.ChipsModel
import com.david.movie.movielab.ui.composable.MovieCard
import com.david.movie.movielab.ui.composable.search.AppSearchBar
import com.david.movie.movielab.ui.screens.ErrorScreen
import com.david.movie.movielab.ui.screens.LoadingScreen
import com.david.movie.notwork.dto.Genres


@Composable
fun DiscoverScreen(
    navController: NavController,
    viewModel: DiscoverViewModel,
    innerPadding: PaddingValues,

    ) {
    val uiState by viewModel.genresState.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()

    val movies = viewModel.personsPagingData.collectAsLazyPagingItems()



    BackHandler(enabled = movies.itemSnapshotList.isNotEmpty()) {
        Log.d("BackHandler", "BackHandler")
        viewModel.handleBack()
        movies.refresh()
        return@BackHandler
    }



    if (movies.itemCount == 0) {
        when (val state = uiState) {
            is UiState.Loading -> LoadingScreen()
            is UiState.Success -> DiscoverView(
                /** show genres view and search bar */
                genres = state.data,
                selectedGenre = selectedGenre,
                navController = navController,
                innerPadding = innerPadding,
                viewModel = viewModel
            )

            is UiState.Error -> ErrorScreen(state.exception.localizedMessage ?: "An error occurred")
        }
    } else {
        BuildMovieResult(viewModel, innerPadding, movies, navController, selectedGenre)


    }


}

@Composable
fun BuildMovieResult(
    viewModel: DiscoverViewModel,
    innerPadding: PaddingValues,
    movies: LazyPagingItems<MovieItem>,
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
                item { AppSpacer(height = 30.dp) }
                item { AppSpacer(height = 30.dp) }
                items(movies.itemCount) { index ->
                    MovieCard(movie = movies[index]!!, onMovieClick = { movieItem ->
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
    val rating = viewModel.selectedRating.collectAsState()

    Column {
        AppSpacer(height = 10.dp)
        AppSearchBar(
            searchable = viewModel,
            hint = "Search Movies"
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
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
                AppSpacer(height = 6.dp)
                AppSwitch(
                    text = "Only high rated movies",
                    checked = rating.value > 8f,
                    onCheckedChange = {
                        if (rating.value > 8f) {
                            viewModel.onRatingSet(2f)
                        } else {
                            viewModel.onRatingSet(8.1f)
                        }
                    }
                )
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
    }
}