package com.david.movie.lab.ui.screens.discover

import android.util.Log
import com.david.movie.lab.BaseViewModel
import com.david.movie.lab.UiState
import com.david.movie.lab.repo.MovieRepo
import com.david.movie.lab.repo.model.MovieItem
import com.david.movie.lab.runIoCoroutine
import com.david.movie.notwork.dto.Genre
import com.david.movie.notwork.dto.Genres
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class DiscoverViewModel @Inject constructor(private val movieRepo: MovieRepo) :
    BaseViewModel() {

    private val genreHashMap: HashMap<Int, String> = HashMap()


    private val _genresState = MutableStateFlow<UiState<Genres>>(UiState.Loading)
    val genresState = _genresState.asStateFlow()

    private val _selectedGenre = MutableStateFlow<List<Int>>(emptyList())
    val selectedGenre = _selectedGenre.asStateFlow()

    private val _discoveredMovies: MutableStateFlow<UiState<List<MovieItem>>> =
        MutableStateFlow(UiState.Loading)
    val discoveredMovies = _discoveredMovies.asStateFlow()

    private val _searchMoviesPreview: MutableStateFlow<UiState<List<String>>> =
        MutableStateFlow(UiState.Loading)
    val searchMoviesPreview = _searchMoviesPreview.asStateFlow()


    init {
        _genresState.value = UiState.Loading
        getPopularPeople()
    }

    private fun getPopularPeople() {
        runIoCoroutine {
            try {
                val popularPeopleList = movieRepo.getMovieGenres() ?: Genres(emptyList())
                _genresState.value = UiState.Success(popularPeopleList)
                popularPeopleList.genres.forEach {
                    genreHashMap[it.id] = it.name
                }
            } catch (e: Exception) {
                _genresState.value = UiState.Error(e)
            }
        }
    }


    fun getGenreName(id: Int): String {
        return genreHashMap[id] ?: ""
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
            val movies = movieRepo.discoverMovies(genreList = _selectedGenre.value)
            _discoveredMovies.value = UiState.Success(movies)
        }
    }

    fun handleBackWithNonEmptyCategories() {
        _discoveredMovies.value = UiState.Loading
    }

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    fun onPreviewText(text: String) {
        _searchText.value = text
        if (text.length % 2 != 0) return

        runIoCoroutine {
            val movies = movieRepo.searchMovies(text)
            _searchMoviesPreview.value = UiState.Success(movies.map { it.title }.distinct())
        }

    }

    fun onSearchText(text: String) {
        _searchText.value = text
        runIoCoroutine {
            val movies = movieRepo.searchMovies(text)
            _discoveredMovies.value = UiState.Success(movies)
        }
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onPreviewText("")
        }
    }

    fun cleanSearchResult() {
        runIoCoroutine {
            delay(500)
            _isSearching.value = false
            onPreviewText("")
            _discoveredMovies.value = UiState.Loading
        }

    }

}