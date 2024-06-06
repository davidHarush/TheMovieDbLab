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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.david.movie.movielab.main.AppRoutes
import com.david.movie.movielab.repo.Mapper.toMovieItem
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.movielab.ui.composable.AppSpacer
import com.david.movie.movielab.ui.composable.ChipsModel
import com.david.movie.movielab.ui.composable.MovieCard
import com.david.movie.movielab.ui.composable.TabsChips
import com.david.movie.movielab.ui.screens.ErrorScreen
import com.david.movie.movielab.ui.screens.LoadingScreen
import com.david.movie.movielab.ui.screens.favorite.FavoriteViewModel
import com.david.movie.movielab.ui.theme.AppColor

@Composable
fun MainTabScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    navController: NavController,
    innerPadding: PaddingValues,
) {
    val movies = mainViewModel.moviePagingData.collectAsLazyPagingItems()
    val selectedChipIndex by mainViewModel.selectedChipIndex.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                mainViewModel.refreshFavorites()  // Refreshing when the view comes to foreground
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    when (movies.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingScreen()
        }

        is LoadState.Error -> {
            val error = (movies.loadState.refresh as LoadState.Error).error
            ErrorScreen(error.localizedMessage ?: "An error occurred")
        }

        is LoadState.NotLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .align(alignment = Alignment.TopCenter)
                        .padding(top = 60.dp, bottom = 20.dp, start = 10.dp, end = 10.dp)
                ) {
                    TabsChips(
                        chipList = listOf(
                            ChipsModel("Popular"),
                            ChipsModel("Top Rating"),
                            ChipsModel("Now Playing"),
                            ChipsModel("My Favorite")
                        ),
                        selectedChipIndex = selectedChipIndex,
                        onChipSelected = { newIndex ->
                            Log.d("MainTabScreen", "StaggeredMovieGrid: $newIndex")
                            mainViewModel.setSelectedChipIndex(newIndex)
                        },
                    )
                }
                if (selectedChipIndex == 3) {
                    FavoriteMoviesScreen(
                        favoriteViewModel = favoriteViewModel,
                        navController = navController,
                        mainViewModel = mainViewModel,
                        innerPadding = innerPadding
                    )
                } else {
                    if (movies.itemCount > 0) {
                        StaggeredMovieGrid(
                            movies = movies,
                            navController = navController,
                            innerPadding = innerPadding,
                            mainViewModel = mainViewModel,
                            favoriteViewModel = favoriteViewModel,
                            selectedChipIndex = selectedChipIndex
                        )
                    } else {
                        LoadingScreen()

                    }
                }
            }
        }

    }

}


@Composable
fun StaggeredMovieGrid(
    movies: LazyPagingItems<MovieItem>,
    navController: NavController,
    innerPadding: PaddingValues,
    mainViewModel: MainViewModel,
    favoriteViewModel: FavoriteViewModel,
    selectedChipIndex: Int
) {

    val favoriteMovies by favoriteViewModel.moviesAsFlow.collectAsStateWithLifecycle(emptyList())


    val gridState = rememberLazyGridState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .align(alignment = Alignment.TopCenter),
                state = gridState
            ) {
                item { AppSpacer(height = 120.dp) }
                item { AppSpacer(height = 120.dp) }
                items(
                    count = movies.itemCount
                ) { index ->
                    MovieCard(
                        movie = movies[index]!!,
                        onMovieClick = { movieItem ->
                            navController.navigate(AppRoutes.movieDetailsRoute(movieId = movieItem.id.toString()))
                        },
                        isFavorite = favoriteMovies.any { it.id == movies[index]?.id },
                        onFavoriteClick = { movieItem, isFavorite ->
                            favoriteViewModel.onFavoriteClick(movieItem, isFavorite)
                        }

                    )

                }

            }

        }
    }
}


@Composable
fun FavoriteMoviesScreen(
    favoriteViewModel: FavoriteViewModel,
    navController: NavController,
    mainViewModel: MainViewModel,
    innerPadding: PaddingValues
) {


    val favoriteMovies by favoriteViewModel.moviesAsFlow.collectAsState(emptyList())
    val gridState = rememberLazyGridState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (favoriteMovies.isEmpty()) {
            Text(
                text = "No favorite movies found.",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 24.sp,
                color = AppColor.LightGreenTMDB
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter),
                state = gridState
            ) {
                item { AppSpacer(height = 120.dp) }
                item { AppSpacer(height = 120.dp) }
                items(
                    count = favoriteMovies.size
                ) { index ->
                    MovieCard(
                        movie = favoriteMovies[index].toMovieItem(),
                        onMovieClick = { movieItem ->
                            navController.navigate(AppRoutes.movieDetailsRoute(movieId = movieItem.id.toString()))
                        },
                        isFavorite = true,
                        onFavoriteClick = { movieItem, isFavorite ->
                            favoriteViewModel.onFavoriteClick(movieItem, isFavorite)
                        }
                    )
                }
            }
        }
    }
}