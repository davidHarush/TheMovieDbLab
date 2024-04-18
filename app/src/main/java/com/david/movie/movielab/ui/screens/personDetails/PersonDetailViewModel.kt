package com.david.movie.movielab.ui.screens.personDetails

import android.util.Log
import androidx.lifecycle.viewModelScope

import com.david.movie.movielab.BaseViewModel
import com.david.movie.movielab.UiState
import com.david.movie.movielab.repo.MovieRepo
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.dto.PersonExternalIdsTMDB
import com.david.movie.notwork.dto.PersonTMDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailViewModel @Inject constructor(private val movieRepo: MovieRepo) :
    BaseViewModel() {


    private val _personState = MutableStateFlow<UiState<PersonTMDB?>>(UiState.Loading)
    val personState: StateFlow<UiState<PersonTMDB?>> get() = _personState


    private val _personIdsState = MutableStateFlow<UiState<PersonExternalIdsTMDB?>>(UiState.Loading)
    val personIdsState: StateFlow<UiState<PersonExternalIdsTMDB?>> get() = _personIdsState


    private val _personMovieState = MutableStateFlow<UiState<List<MovieItem>?>>(UiState.Loading)
    val personMovieState: StateFlow<UiState<List<MovieItem>?>> get() = _personMovieState

    init {
        _personState.value = UiState.Loading
    }

    fun getPersonDetails(personId: Int) {
        viewModelScope.launch {
            try {
                val movieDetails = movieRepo.getPersonDetails(personId)
                _personState.value = UiState.Success(movieDetails)
            } catch (e: Exception) {
                _personState.value = UiState.Error(e)
                Log.e("DetailsViewModel", "getPersonDetails: ", e)
            }
        }
    }


    fun getPersonIds(movieId: Int) {
        viewModelScope.launch {
            try {
                _personIdsState.value = UiState.Success(movieRepo.getPersonIds(movieId))
            } catch (e: Exception) {
                Log.e("DetailsViewModel", "getPersonIds: ", e)
            }
        }
    }

    fun getPersonMovies(movieId: Int) {
        viewModelScope.launch {
            try {
                _personMovieState.value = UiState.Success(movieRepo.getPersonMovies(movieId))
            } catch (e: Exception) {
                Log.e("DetailsViewModel", "getPersonMovies: ", e)
            }
        }
    }

}
