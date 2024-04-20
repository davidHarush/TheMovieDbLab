package com.david.movie.movielab.ui.screens.popularPeople

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.david.movie.movielab.repo.PersonRepo
import com.david.movie.movielab.repo.model.Actor
import com.david.movie.movielab.repo.paging.PagingRepo
import com.david.movie.movielab.ui.composable.search.SearchableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class PopularPeopleViewModel @Inject constructor(
    private val pagingRepo: PagingRepo,
    private val personRepo: PersonRepo
) :
    SearchableViewModel() {

    enum class ActionState {
        PopularPersons, SearchPersons,
    }

    private val actionState = MutableStateFlow(ActionState.PopularPersons)


    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> = _navigateBack


    private val searchQuery = MutableStateFlow("")


    @OptIn(ExperimentalCoroutinesApi::class)
    val personsPagingData: Flow<PagingData<Actor>> = searchQuery.flatMapLatest { query ->
        if (actionState.value == ActionState.SearchPersons) {
            pagingRepo.searchPersonsStream(query).cachedIn(viewModelScope)
        } else {
            pagingRepo.getPopularPersonsStream().cachedIn(viewModelScope)
        }
    }

    fun handleBack() {
        if (searchQuery.value.isNotEmpty()) {
            searchQuery.update { "" }
            _navigateBack.value = false
            actionState.update { ActionState.PopularPersons }
        } else {
            _navigateBack.value = true
        }
    }


    override suspend fun doSearch(query: String) {
        actionState.update { ActionState.SearchPersons }
        searchQuery.update { query }
        Log.d("DiscoverViewModel", "doSearch: $query")

    }


    override suspend fun onSearchPreview(query: String): List<String> {
        Log.d("DiscoverViewModel", "onSearchPreview: $query")

        val persons = personRepo.searchPersons(query)?.results ?: emptyList()
        val sortedPersons =
            persons.filter { it.profile_path != null }.sortedByDescending { it.popularity }
        return sortedPersons.mapNotNull { it.name }.distinct()

    }


}


