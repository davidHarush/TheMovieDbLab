package com.david.movie.lab.ui.screens.main


import android.content.ClipData.Item
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.david.movie.lab.UiState
import com.david.movie.lab.main.Destinations
import com.david.movie.lab.repo.model.MovieItem
import com.david.movie.lab.ui.composable.AppSpacer
import com.david.movie.lab.ui.composable.ChipsModel
import com.david.movie.lab.ui.composable.MovieCard
import com.david.movie.lab.ui.composable.TabsChips
import com.david.movie.lab.ui.screens.ErrorScreen
import com.david.movie.lab.ui.screens.LoadingScreen


@Composable
fun MainTabScreen(
    viewModel: MainViewModel = viewModel(),
    navController: NavController,
    innerPadding: PaddingValues,
) {
    val movieListState by viewModel.movieListState.collectAsState()
    val selectedChipIndex by viewModel.selectedChipIndex.collectAsState()

    when (val state = movieListState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Success -> StaggeredMovieGrid(
            movies = state.data,
            navController = navController,
            innerPadding = innerPadding,
            viewModel = viewModel
        )

        is UiState.Error -> ErrorScreen(state.exception.localizedMessage ?: "An error occurred")
    }
}

@Composable
fun StaggeredMovieGrid(
    movies: List<MovieItem>,
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: MainViewModel,

    ) {

    val selectedChipIndex by viewModel.selectedChipIndex.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()

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
                item { AppSpacer(height = 80.dp) }
                item { AppSpacer(height = 80.dp) }
                items(movies) { movie ->
                    MovieCard(movie = movie, onMovieClick = { movieItem ->
                        navController.navigate(Destinations.movieDetailsRoute(movieId =  movieItem.id.toString()))
                    })

                }
                if (isLoadingMore) {
                    item { CircularProgressIndicator() }
                    item { CircularProgressIndicator() }
                    item { AppSpacer(height =  80.dp) }
                    item { AppSpacer(height =  80.dp) }
                }

            }

            LaunchedEffect(gridState) {
                gridState.interactionSource.interactions.collect {
                    if (gridState.isScrolledToEnd()) {
                        viewModel.fetchNextPage()
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.3f))
                .align(alignment = Alignment.TopCenter)
                .padding(top = 60.dp, bottom = 20.dp, start = 10.dp, end = 10.dp)
        ) {
            TabsChips(
                chipList = listOf(ChipsModel("Popular"), ChipsModel("Top Rating")),
                selectedChipIndex = selectedChipIndex,
                onChipSelected = { newIndex ->
                    Log.d("MainTabScreen", "StaggeredMovieGrid: $newIndex")
                    viewModel.setSelectedChipIndex(newIndex)

                }
            )
        }
    }
}

fun LazyGridState.isScrolledToEnd(threshold: Int = 4): Boolean {
    val layoutInfo = this.layoutInfo
    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return false
    val totalItemCount = layoutInfo.totalItemsCount
    return lastVisibleItemIndex >= (totalItemCount - 1 - threshold)
}

@Composable
fun LoadingFooter() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(80.dp), color = Color.White)
    }
}

