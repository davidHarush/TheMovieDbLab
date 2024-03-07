package com.david.movie.lab.ui.screens.main

import android.util.Log
import com.david.movie.lab.BaseViewModel
import com.david.movie.lab.UiState
import com.david.movie.lab.repo.MovieRepo
import com.david.movie.lab.repo.model.MovieItem
import com.david.movie.lab.runIoCoroutine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val movieRepo: MovieRepo) : BaseViewModel() {

    private val _movieListState = MutableStateFlow<UiState<List<MovieItem>>>(UiState.Loading)
    val movieListState: StateFlow<UiState<List<MovieItem>>> get() = _movieListState

    private val _selectedChipIndex = MutableStateFlow(0)
    val selectedChipIndex: StateFlow<Int> = _selectedChipIndex.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()


    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false

    init {
        fetchMovies()
    }

    private fun fetchMovies(page: Int = currentPage, onComplete: () -> Unit = {}) {

//        runIoCoroutine {
//
//            val pp = movieRepo.getPopularPerson()
//
//            Log.d("getPopularPerson", "getPopularPerson: $pp")
//        }
//

        if (isLoading) return

        if (page == 1) {
            _movieListState.value = UiState.Loading
        } else {
            _isLoadingMore.value = true
        }

        isLoading = true



        runIoCoroutine {
            try {
                val movieList = if (_selectedChipIndex.value == 0) {
                    movieRepo.getPopularMovieList(page)
                } else {
                    movieRepo.getTopRatedMovieList(page)
                }

                if (movieList.isEmpty()) {
                    isLastPage = true
                } else {
                    currentPage = page
                }

                val currentMovies =
                    (_movieListState.value as? UiState.Success)?.data.orEmpty().toMutableList()
                currentMovies.addAll(movieList)
                _movieListState.value = UiState.Success(currentMovies)
            } catch (e: Exception) {
                _movieListState.value = UiState.Error(e)
            } finally {
                isLoading = false
                onComplete() // Call the completion callback
            }
        }
    }

    fun fetchNextPage() {
        if (!isLoading && !isLastPage) {
            _isLoadingMore.value = true // Start loading more
            fetchMovies(currentPage + 1) {
                _isLoadingMore.value = false // Stop loading more
            }
        }
    }


    fun setSelectedChipIndex(newIndex: Int) {
        if (_selectedChipIndex.value != newIndex) {
            _selectedChipIndex.value = newIndex
            resetPagination()
            fetchMovies()
        }
    }

    private fun resetPagination() {
        currentPage = 1
        isLastPage = false
        _movieListState.value = UiState.Loading // Clear current movie list state
    }

}
