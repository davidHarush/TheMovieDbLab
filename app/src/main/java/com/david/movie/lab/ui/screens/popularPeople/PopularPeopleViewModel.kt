package com.david.movie.lab.ui.screens.popularPeople

import com.david.movie.lab.BaseViewModel
import com.david.movie.lab.UiState
import com.david.movie.lab.repo.MovieRepo
import com.david.movie.lab.repo.model.Actor
import com.david.movie.lab.runIoCoroutine
import com.david.movie.notwork.dto.PersonTMDB
import com.david.movie.notwork.dto.PopularPersonList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class PopularPeopleViewModel @Inject constructor(private val movieRepo: MovieRepo) :
    BaseViewModel() {


    private val _personState = MutableStateFlow<UiState<List<Actor>?>>(UiState.Loading)
    val personState: StateFlow<UiState<List<Actor>?>> get() = _personState


    init {
        _personState.value = UiState.Loading
        getPopularPeople()
    }

    fun getPopularPeople() {
        runIoCoroutine {
            try {
                val popularPeopleList = movieRepo.getPopularPerson() // Assuming this returns PopularPersonList
                val actors = popularPeopleList?.results?.map { person ->
                    // Directly create an Actor instance from PopularPersonTMDB
                    Actor(
                        gender = person.gender,
                        id = person.id,
                        known_for_department = person.known_for_department,
                        name = person.name,
                        original_name = person.name, // Assuming you want to use 'name' for 'original_name'
                        popularity = person.popularity,
                        profile_path = person.profile_path,
                        cast_id = null, // Default or null if not applicable
                        character = null, // Default or null if not applicable
                        credit_id = null, // Default or null if not applicable
                        order = null // Default or null if not applicable
                    )
                }
                _personState.value = UiState.Success(actors)
            } catch (e: Exception) {
                _personState.value = UiState.Error(e)
            }
        }
    }



}