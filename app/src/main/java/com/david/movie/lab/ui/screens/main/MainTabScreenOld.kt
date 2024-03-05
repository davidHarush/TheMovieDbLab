//package com.david.cinema.ui.screens.main

//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.*
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.zIndex
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.david.movie.lab.UiState
//import com.david.cinema.repo.model.MovieItem
//import com.david.cinema.repo.model.isEmpty
//import com.david.movie.lab.ui.composable.AppSpacer
//import com.david.cinema.ui.composable.ChipsModel
//import com.david.cinema.ui.composable.MovieCard
//import com.david.cinema.ui.composable.TabsChips
//import com.david.movie.lab.ui.screens.ErrorScreen
//import com.david.movie.lab.ui.screens.LoadingScreen
//
//@Composable
//fun MainTabScreenOld(
//    viewModel: MainViewModel = viewModel(),
//    navController: NavController,
//    innerPadding: PaddingValues,
//) {
//    val movieListState by viewModel.movieListState.collectAsState()
//    val selectedChipIndex by viewModel.selectedChipIndex.collectAsState()
//
//
//
//    when (val state = movieListState) {
//        is UiState.Loading -> LoadingScreen()
//        is UiState.Success -> StaggeredMovieGrid(
//            movies = state.data,
//            navController = navController,
//            innerPadding = innerPadding,
//            viewModel = viewModel
//        )
//        is UiState.Error -> ErrorScreen(state.exception.localizedMessage ?: "An error occurred")
//    }
//}
//
//
//
//
//@Composable
//fun StaggeredMovieGrid(
//    movies: List<MovieItem>,
//    navController: NavController,
//    innerPadding: PaddingValues,
//    viewModel: MainViewModel,
//
//    ) {
//
//    val selectedChipIndex by viewModel.selectedChipIndex.collectAsState()
//    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
//
//    val gridState = rememberLazyGridState()
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//
//        Box(modifier = Modifier.fillMaxWidth().height(100.dp).zIndex(10f)
//            .align(Alignment.BottomCenter), contentAlignment = Alignment.Center) {
//            if (isLoadingMore) {
//                LoadingFooter()
//            }
//        }
//
//        Box {
//            LazyVerticalGrid(
//                columns = GridCells.Adaptive(minSize = 180.dp),
//                contentPadding = PaddingValues(16.dp),
//                modifier = Modifier
//                    .fillMaxSize()
//                    .align(alignment = Alignment.TopCenter),
//                state = gridState
//            ) {
//                item { AppSpacer(height = 80.dp) } //  space in the top of the screen
//                item { AppSpacer(height = 80.dp) } //  space in the top of the screen
//                items(movies) { movie ->
//                    MovieCard(movie = movie, onMovieClick = { movieItem ->
//                        navController.navigate("movieDetails/${movieItem.id}")
//                    })
//                }
//
//            }
//
//            LaunchedEffect(gridState) {
//                gridState.interactionSource.interactions.collect {
//                    if (gridState.isScrolledToEnd()) {
//                        viewModel.fetchNextPage()
//                    }
//                }
//            }
//        }
//
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Black.copy(alpha = 0.3f))
//                .align(alignment = Alignment.TopCenter)
//                .padding(top = 40.dp, bottom = 20.dp, start = 10.dp, end = 10.dp)
//        ) {
//            TabsChips(
//                chipList = listOf(ChipsModel("Popular"), ChipsModel("Top Rating")),
//                selectedChipIndex = selectedChipIndex,
//                onChipSelected = { newIndex ->
//                    Log.d("MainTabScreen", "StaggeredMovieGrid: $newIndex")
//                    viewModel.setSelectedChipIndex(newIndex)
//
//                }
//            )
//        }
//    }
//}
//
//fun LazyGridState.isScrolledToEnd(threshold: Int = 4): Boolean {
//    val layoutInfo = this.layoutInfo
//    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return false
//    val totalItemCount = layoutInfo.totalItemsCount
//    return lastVisibleItemIndex >= (totalItemCount - 1 - threshold)
//}
//
//@Composable
//fun LoadingFooter() {
//    Box(modifier = Modifier
//        .fillMaxWidth()
//        .padding(16.dp), contentAlignment = Alignment.Center) {
//        CircularProgressIndicator(modifier = Modifier.size(80.dp), color = Color.White)
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//import com.david.movie.lab.BaseViewModel
//import com.david.movie.lab.UiState
//import com.david.cinema.repo.MovieRepo
//import com.david.cinema.repo.model.MovieItem
//import com.david.cinema.runIoCoroutine
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import javax.inject.Inject
//
//
//@HiltViewModel
//class MainViewModel @Inject constructor(private val movieRepo: MovieRepo) : BaseViewModel() {
//
//    private val _movieListState = MutableStateFlow<UiState<List<MovieItem>>>(UiState.Loading)
//    val movieListState: StateFlow<UiState<List<MovieItem>>> get() = _movieListState
//
//    private val _selectedChipIndex = MutableStateFlow(0)
//    val selectedChipIndex: StateFlow<Int> = _selectedChipIndex.asStateFlow()
//
//    private val _isLoadingMore = MutableStateFlow(false)
//    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()
//
//
//    private var currentPage = 1
//    private var isLastPage = false
//    private var isLoading = false
//
//    init {
//        fetchMovies()
//    }
//    private fun fetchMovies(page: Int = currentPage, onComplete: () -> Unit = {}) {
//        if (isLoading) return
//
//        if (page == 1) {
//            _movieListState.value = UiState.Loading
//        } else {
//            _isLoadingMore.value = true
//        }
//
//        isLoading = true
//
//        runIoCoroutine {
//            try {
//                val movieList = if (_selectedChipIndex.value == 0) {
//                    movieRepo.getPopularMovieList(page)
//                } else {
//                    movieRepo.getTopRatedMovieList(page)
//                }
//
//                if (movieList.isEmpty()) {
//                    isLastPage = true
//                } else {
//                    currentPage = page
//                }
//
//                val currentMovies = (_movieListState.value as? UiState.Success)?.data.orEmpty().toMutableList()
//                currentMovies.addAll(movieList)
//                _movieListState.value = UiState.Success(currentMovies)
//            } catch (e: Exception) {
//                _movieListState.value = UiState.Error(e)
//            } finally {
//                isLoading = false
//                onComplete() // Call the completion callback
//            }
//        }
//    }
//
//    fun fetchNextPage() {
//        if (!isLoading && !isLastPage) {
//            _isLoadingMore.value = true // Start loading more
//            fetchMovies(currentPage + 1) {
//                _isLoadingMore.value = false // Stop loading more
//            }
//        }
//    }
//
//
//    fun setSelectedChipIndex(newIndex: Int) {
//        if (_selectedChipIndex.value != newIndex) {
//            _selectedChipIndex.value = newIndex
//            resetPagination()
//            fetchMovies()
//        }
//    }
//
//    private fun resetPagination() {
//        currentPage = 1
//        isLastPage = false
//        _movieListState.value = UiState.Loading // Clear current movie list state
//    }
//}
