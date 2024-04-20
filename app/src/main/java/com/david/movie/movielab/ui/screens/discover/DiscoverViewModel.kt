package com.david.movie.movielab.ui.screens.discover

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.david.movie.movielab.UiState
import com.david.movie.movielab.repo.MovieRepo
import com.david.movie.movielab.repo.PagingRepo
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.movielab.runIoCoroutine
import com.david.movie.movielab.ui.composable.search.Searchable
import com.david.movie.movielab.ui.composable.search.SearchableViewModel
import com.david.movie.notwork.dto.Genre
import com.david.movie.notwork.dto.Genres
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val pagingRepo: PagingRepo,
    private val movieRepo: MovieRepo
) :
    SearchableViewModel(), Searchable {
    private enum class ActionState {
        Discover, Search, None
    }

    private val genreHashMap: HashMap<Int, String> = HashMap()

    private val _genresState = MutableStateFlow<UiState<Genres>>(UiState.Loading)
    val genresState = _genresState.asStateFlow()

    private val _selectedGenre = MutableStateFlow<List<Int>>(emptyList())
    val selectedGenre = _selectedGenre.asStateFlow()

    private val _selectedRating = MutableStateFlow(7f)
    val selectedRating = _selectedRating.asStateFlow()

    private val searchQuery = MutableStateFlow("")

    private val actionState = MutableStateFlow(ActionState.None)


    @OptIn(ExperimentalCoroutinesApi::class)
    val personsPagingData: Flow<PagingData<MovieItem>> = actionState.flatMapLatest { state ->
        when (state) {
            ActionState.Search -> {
                searchQuery.flatMapLatest { query ->
                    pagingRepo.getSearchMoviesStream(query).cachedIn(viewModelScope)
                }
            }

            ActionState.Discover -> {
                pagingRepo.getDiscoverMoviesStream(
                    genreList = _selectedGenre.value,
                    rating = _selectedRating.value
                ).cachedIn(viewModelScope)
            }

            ActionState.None -> {
                Log.d("BackHandler", "personsPagingData: emptyFlow()")
                flowOf(PagingData.empty())
            }

        }
    }


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

    fun onRatingSet(newValue: Float) {
        _selectedRating.update { newValue }
    }


    fun onSelectedGenre(it: Genre) {
        val list = _selectedGenre.value.toMutableList()
        if (list.contains(it.id)) {
            list.remove(it.id)
        } else {
            list.add(it.id)
        }
        _selectedGenre.update { list }
        Log.d("DiscoverViewModel", "onSelectedGenre: ${_selectedGenre.value}")

    }

    fun onDiscoverClicked() {
        actionState.update { ActionState.Discover }
    }

    fun handleBack() {
        actionState.update { ActionState.None }
    }


//============== SearchableViewModel ===================

    override suspend fun doSearch(query: String) {
        actionState.value = ActionState.Search
        searchQuery.update { query }
        Log.d("DiscoverViewModel", "doSearch: $query")

    }

    override suspend fun onSearchPreview(query: String): List<String> {
        val movies = movieRepo.searchMovies(query)
        return movies.map { it.title }.distinct()

    }


}