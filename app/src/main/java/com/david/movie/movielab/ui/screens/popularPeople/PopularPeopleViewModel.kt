package com.david.movie.movielab.ui.screens.popularPeople

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.david.movie.movielab.UiState
import com.david.movie.movielab.repo.MovieRepo
import com.david.movie.movielab.repo.model.Actor
import com.david.movie.movielab.runIoCoroutine
import com.david.movie.movielab.ui.composable.search.SearchableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class PopularPeopleViewModel @Inject constructor(private val movieRepo: MovieRepo) :
    SearchableViewModel() {


    private val _personState = MutableStateFlow<UiState<List<Actor>?>>(UiState.Loading)
    val personState = _personState.asStateFlow()


    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> = _navigateBack

    private var actors: List<Actor> = emptyList()

    init {
        _personState.value = UiState.Loading
        getPopularPeople()
    }

    private val searchQuery = MutableStateFlow("")


    @OptIn(ExperimentalCoroutinesApi::class)
    val personsPagingData: Flow<PagingData<Actor>> = searchQuery.flatMapLatest { query ->
        if (query.isNotEmpty()) {
            movieRepo.searchPersonsStream(query).cachedIn(viewModelScope)
        } else {
            movieRepo.getPopularPersonsStream().cachedIn(viewModelScope)
        }
    }


    private fun getPopularPeople() {
        runIoCoroutine {
            try {
                val popularPeopleList =
                    movieRepo.getPopularPerson()
                actors = popularPeopleList?.results
                    ?.map { person -> Actor(person) }
                    ?.filter { it.profile_path != null } ?: emptyList()

                _personState.value = UiState.Success(actors)
            } catch (e: Exception) {
                _personState.value = UiState.Error(e)
            }
        }
    }

    fun handleBack() {
        if (searchQuery.value.isNotEmpty()) {
            searchQuery.value = ""
            _navigateBack.value = false
        } else {
            _navigateBack.value = true
        }
    }


    override suspend fun doSearch(query: String) {
        searchQuery.update { query }
        Log.d("DiscoverViewModel", "doSearch: $query")

    }


    override suspend fun onSearchPreview(query: String): List<String> {
        Log.d("DiscoverViewModel", "onSearchPreview: $query")

        val persons = movieRepo.searchPersons(query)?.results ?: emptyList()
        val sortedPersons =
            persons.filter { it.profile_path != null }.sortedByDescending { it.popularity }
        return sortedPersons.mapNotNull { it.name }.distinct()

    }


}


