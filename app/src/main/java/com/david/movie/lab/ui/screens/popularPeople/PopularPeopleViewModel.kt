package com.david.movie.lab.ui.screens.popularPeople

import com.david.movie.lab.UiState
import com.david.movie.lab.repo.MovieRepo
import com.david.movie.lab.repo.model.Actor
import com.david.movie.lab.runIoCoroutine
import com.david.movie.lab.ui.composable.search.SearchableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class PopularPeopleViewModel @Inject constructor(private val movieRepo: MovieRepo) :
    SearchableViewModel() {


    private val _personState = MutableStateFlow<UiState<List<Actor>?>>(UiState.Loading)
    val personState = _personState.asStateFlow()
    private var actors: List<Actor> = emptyList()

    init {
        _personState.value = UiState.Loading
        getPopularPeople()
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
        _personState.value = UiState.Success(actors)
    }


    override suspend fun doSearch(query: String) {
        val result = movieRepo.searchPersons(query)?.results

        val actors = result
            ?.map { person -> Actor(person) }
            ?.filter { it.profile_path != null }
        val sortedActors = actors?.sortedByDescending { it.popularity }
            ?.distinctBy { it.id } ?: emptyList()
        _personState.value = UiState.Success(sortedActors)
        onToggleSearch()
    }


    override suspend fun onSearchPreview(query: String): List<String> {
        val persons = movieRepo.searchPersons(query)?.results ?: emptyList()
        val sortedPersons = persons.sortedByDescending { it.popularity }
        return sortedPersons.mapNotNull { it.name }.distinct()

    }

}
