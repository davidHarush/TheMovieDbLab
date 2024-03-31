package com.david.movie.lab.ui.screens.discover

import android.util.Log
import com.david.movie.lab.UiState
import com.david.movie.lab.repo.MovieRepo
import com.david.movie.lab.repo.model.MovieItem
import com.david.movie.lab.runIoCoroutine
import com.david.movie.lab.ui.composable.search.Searchable
import com.david.movie.lab.ui.composable.search.SearchableViewModel
import com.david.movie.notwork.dto.Genre
import com.david.movie.notwork.dto.Genres
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(private val movieRepo: MovieRepo) :
    SearchableViewModel(), Searchable {


    private val genreHashMap: HashMap<Int, String> = HashMap()


    private val _genresState = MutableStateFlow<UiState<Genres>>(UiState.Loading)
    val genresState = _genresState.asStateFlow()

    private val _selectedGenre = MutableStateFlow<List<Int>>(emptyList())
    val selectedGenre = _selectedGenre.asStateFlow()

    private val _discoveredMovies: MutableStateFlow<UiState<List<MovieItem>>> =
        MutableStateFlow(UiState.Loading)
    val discoveredMovies = _discoveredMovies.asStateFlow()


    init {
        _genresState.value = UiState.Loading
        getGenresList()
    }

    private fun getGenresList() {
        runIoCoroutine {
            try {
                val movieGenresList = movieRepo.getMovieGenres() ?: Genres(emptyList())
                _genresState.value = UiState.Success(movieGenresList)
                movieGenresList.genres.forEach {
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

            val filteredMovies = movies
                .filter { it.poster_path?.isNotEmpty() ?: false && it.backdrop_path?.isNotEmpty() ?: false }
                .sortedByDescending { it.voteAverage }
                .distinctBy { it.id } ?: emptyList()
            _discoveredMovies.value = UiState.Success(filteredMovies)

        }
    }

    fun handleBackWithNonEmptyCategories() {
        _discoveredMovies.value = UiState.Loading
    }


//============== SearchableViewModel ===================

    override suspend fun doSearch(query: String) {
        val movies = movieRepo.searchMovies(query)
        _discoveredMovies.value = UiState.Success(movies)

    }

    override suspend fun onSearchPreview(query: String): List<String> {
        val movies = movieRepo.searchMovies(query)
        return movies.map { it.title }.distinct()

    }

}