package com.david.movie.lab.ui.screens.discover

import android.util.Log
import com.david.movie.lab.BaseViewModel
import com.david.movie.lab.UiState
import com.david.movie.lab.repo.MovieRepo
import com.david.movie.lab.runIoCoroutine
import com.david.movie.notwork.dto.Genre
import com.david.movie.notwork.dto.Genres
import com.david.movie.notwork.dto.MovieList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class DiscoverViewModel @Inject constructor(private val movieRepo: MovieRepo) :
    BaseViewModel() {


    private val _genresState = MutableStateFlow<UiState<Genres?>>(UiState.Loading)
    val genresState: StateFlow<UiState<Genres?>> get() = _genresState

    private val _selectedGenre = MutableStateFlow<List<Int>>(emptyList())
    val selectedGenre : StateFlow<List<Int>> get() = _selectedGenre

    private val _discoveredMovies : MutableStateFlow<UiState<MovieList?>> = MutableStateFlow(UiState.Loading)
    val discoveredMovies : StateFlow<UiState<MovieList?>> = _discoveredMovies


    init {
        _genresState.value = UiState.Loading
        getPopularPeople()
    }

    private fun getPopularPeople() {
        runIoCoroutine {
            try {
                val popularPeopleList = movieRepo.getMovieGenres() // Assuming this returns PopularPersonList
                _genresState.value = UiState.Success(popularPeopleList)
            } catch (e: Exception) {
                _genresState.value = UiState.Error(e)
            }
        }
    }

    fun onSelectedGenre(it: Genre) {
        val list = _selectedGenre.value.toMutableList()
        if (list.contains(it.id)) {
            list.remove(it.id)
        } else {
            list.add(it.id)
        }
        _selectedGenre.value = list
        Log.d("DiscoverViewModel", "onSelectedGenre: ${_selectedGenre.value}")

    }

    fun onDiscoverClicked() {
        runIoCoroutine {
          val movies =  movieRepo.discoverMovies( genreList = _selectedGenre.value)
            _discoveredMovies.value = UiState.Success(movies)
        }

    }

    fun handleBackWithNonEmptyCategories() {
        _discoveredMovies.value = UiState.Loading
    }


}
