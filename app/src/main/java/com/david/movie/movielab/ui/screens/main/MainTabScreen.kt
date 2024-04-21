package com.david.movie.movielab.ui.screens.main


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.david.movie.movielab.main.AppRoutes
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.movielab.ui.composable.AppSpacer
import com.david.movie.movielab.ui.composable.ChipsModel
import com.david.movie.movielab.ui.composable.MovieCard
import com.david.movie.movielab.ui.composable.TabsChips
import com.david.movie.movielab.ui.screens.ErrorScreen
import com.david.movie.movielab.ui.screens.LoadingScreen

@Composable
fun MainTabScreen(
    viewModel: MainViewModel = viewModel(),
    navController: NavController,
    innerPadding: PaddingValues,
) {
    val movies = viewModel.moviePagingData.collectAsLazyPagingItems()


    when (movies.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingScreen()
        }

        is LoadState.Error -> {
            val error = (movies.loadState.refresh as LoadState.Error).error
            ErrorScreen(error.localizedMessage ?: "An error occurred")
        }

        is LoadState.NotLoading -> {
            if (movies.itemCount > 0) {
                StaggeredMovieGrid(
                    movies = movies,
                    navController = navController,
                    innerPadding = innerPadding,
                    viewModel = viewModel
                )
            } else {
                LoadingScreen()
            }
        }
    }

}


@Composable
fun StaggeredMovieGrid(
    movies: LazyPagingItems<MovieItem>,
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: MainViewModel,

    ) {

    val selectedChipIndex by viewModel.selectedChipIndex.collectAsState()


    val gridState = rememberLazyGridState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 180.dp),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .align(alignment = Alignment.TopCenter),
                state = gridState
            ) {
                item { AppSpacer(height = 120.dp) }
                item { AppSpacer(height = 120.dp) }
                items(movies.itemCount) { index ->
                    MovieCard(movie = movies[index]!!, onMovieClick = { movieItem ->
                        navController.navigate(AppRoutes.movieDetailsRoute(movieId = movieItem.id.toString()))
                    })

                }

            }

        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.6f))
                .align(alignment = Alignment.TopCenter)
                .padding(top = 60.dp, bottom = 20.dp, start = 10.dp, end = 10.dp)
        ) {
            TabsChips(
                chipList = listOf(
                    ChipsModel("Popular"),
                    ChipsModel("Top Rating"),
                    ChipsModel("Now Playing")
                ),
                selectedChipIndex = selectedChipIndex,
                onChipSelected = { newIndex ->
                    Log.d("MainTabScreen", "StaggeredMovieGrid: $newIndex")
                    viewModel.setSelectedChipIndex(newIndex)

                }
            )
        }
    }
}