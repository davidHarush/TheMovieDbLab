package com.david.movie.lab.ui.screens.popularPeople

import com.david.movie.lab.BaseViewModel
import com.david.movie.lab.UiState
import com.david.movie.lab.repo.MovieRepo
import com.david.movie.lab.repo.model.Actor
import com.david.movie.lab.runIoCoroutine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class PopularPeopleViewModel @Inject constructor(private val movieRepo: MovieRepo) :
    BaseViewModel() {


    private val _personState = MutableStateFlow<UiState<List<Actor>?>>(UiState.Loading)
    val personState: StateFlow<UiState<List<Actor>?>> get() = _personState


    private val _searchPersonPreview = MutableStateFlow<UiState<List<String?>>>(UiState.Loading)
    val searchPersonPreview = _searchPersonPreview.asStateFlow()


    init {
        _personState.value = UiState.Loading
        getPopularPeople()
    }

    fun getPopularPeople() {
        runIoCoroutine {
            try {
                val randomPage = (0..5).random()
                val popularPeopleList =
                    movieRepo.getPopularPerson() // Assuming this returns PopularPersonList
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








    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    fun onPreviewText(text: String) {
        _searchText.value = text
        runIoCoroutine {
            val persons = movieRepo.searchPersons(text)?.results ?: emptyList()
            _searchPersonPreview.value = UiState.Success(persons.map { it.name }.distinct())
        }

    }


    fun onSearchText(text: String) {
        _searchText.value = text
        runIoCoroutine {
            val movies = movieRepo.searchPersons(text)
            val actors = movies?.results?.map { person ->
                Actor(
                    gender = person.gender,
                    id = person.id,
                    known_for_department = person.known_for_department,
                    name = person.name,
                    original_name = person.name,
                    popularity = person.popularity,
                    profile_path = person.profile_path,
                    cast_id = null,
                    character = null,
                    credit_id = null,
                    order = null
                )
            }
            _personState.value = UiState.Success(actors)
           onToggleSearch()

        }

    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
//        if (_isSearching.value == false) {
//            onPreviewText("")
//        }
    }

}


//2024-03-24 22:47:05.324 20056-20124 System.out              com.david.movie.lab                  I  JSON input: .....89,"known_for_department":null,"name":"T","original_name":"T.....TMDBService - call :RESPONSE https://api.themoviedb.org/3/search/person?api_key=56a778f90174e0061b6e7c69a5e3c9f2&language=en-US&page=1&query=tom failed with exception: kotlinx.serialization.json.internal.JsonDecodingException:
// Unexpected JSON token at offset 718: Unexpected 'null' value instead of string literal