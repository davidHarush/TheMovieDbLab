package com.david.movie.lab.ui.screens.discover

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.david.movie.lab.UiState
import com.david.movie.lab.main.AppRoutes
import com.david.movie.lab.repo.model.MovieItem
import com.david.movie.lab.ui.composable.ActionButton
import com.david.movie.lab.ui.composable.AppSpacer
import com.david.movie.lab.ui.composable.ChipsGrid
import com.david.movie.lab.ui.composable.ChipsModel
import com.david.movie.lab.ui.composable.MovieCard
import com.david.movie.lab.ui.composable.search.AppSearchBar
import com.david.movie.lab.ui.screens.ErrorScreen
import com.david.movie.lab.ui.screens.LoadingScreen
import com.david.movie.lab.ui.theme.AppColor
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
                /** show genres view and search bar */
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
    val rating = viewModel.selectedRating.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AppSpacer(height = 10.dp)
        AppSearchBar(
            searchable = viewModel,
            hint = "Search Movies"
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
        AppSpacer(height = 6.dp)
//        RatingSlider(
//            stateValue = viewModel.selectedRating.collectAsState()
//        ) { newValue ->
//            viewModel.onRatingSet(newValue)
//        }
        HorizontalDivider(
            color = Color.Cyan.copy(0.5f),
            thickness = 1.dp,
            modifier = Modifier.padding(16.dp),
        )


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Only high rated movies")
            AppSpacer(width = 16.dp)
            Switch(
                checked = rating.value > 8f,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = AppColor.LightGreenTMDB.copy(alpha = 0.8f),
                    checkedTrackColor = AppColor.TwilightBlue,
                    checkedBorderColor =AppColor.TwilightBlue,

                    uncheckedThumbColor = AppColor.DarkBlueTMDB,
                    uncheckedTrackColor = AppColor.TwilightBlue,
                    uncheckedBorderColor = AppColor.TwilightBlue,
                ),

                onCheckedChange = {
                    if (rating.value > 8f) {
                        viewModel.onRatingSet(2f)
                    } else {
                        viewModel.onRatingSet(8.1f)
                    }
                }
            )
        }



        AppSpacer(height = 6.dp)
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


@Composable
fun RatingSlider(
    stateValue: State<Float>,
    onValueChange: (Float) -> Unit
) {
    Slider(
        modifier = Modifier.padding(horizontal = 16.dp),
        value = stateValue.value,
        onValueChange = onValueChange,
        valueRange = 0f..10f,
        steps = 10,
        colors = SliderDefaults.colors(
            thumbColor = Color.Cyan.copy(alpha = 0.8f),
            activeTrackColor = Color.Cyan.copy(alpha = 0.6f),
            inactiveTrackColor = Color.Gray
        )
    )
}
