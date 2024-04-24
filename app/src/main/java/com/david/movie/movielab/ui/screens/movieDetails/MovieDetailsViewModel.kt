package com.david.movie.movielab.ui.screens.movieDetails

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.david.movie.movielab.BaseViewModel
import com.david.movie.movielab.UiState
import com.david.movie.movielab.repo.Mapper.convertToMovieDetailsItem
import com.david.movie.movielab.repo.MovieRepo
import com.david.movie.movielab.repo.model.Actor
import com.david.movie.movielab.repo.model.FullMovieCollection
import com.david.movie.movielab.repo.model.MovieDetailsItem
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.dto.ImagesResponseTMDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val movieRepo: MovieRepo) :
    BaseViewModel() {
    private val _movieState = MutableStateFlow<UiState<MovieDetailsItem>>(UiState.Loading)
    val movieDetailsState: StateFlow<UiState<MovieDetailsItem>> get() = _movieState

    private val _movieCastState = MutableStateFlow<UiState<List<Actor>>>(UiState.Loading)
    val movieCastState: StateFlow<UiState<List<Actor>>> get() = _movieCastState

    private val _similarMoviesState = MutableStateFlow<UiState<List<MovieItem>?>>(UiState.Loading)
    val similarMoviesState: StateFlow<UiState<List<MovieItem>?>> get() = _similarMoviesState


    private val _collectionMoviesState =
        MutableStateFlow<UiState<FullMovieCollection>>(UiState.Loading)
    val collectionMoviesState: StateFlow<UiState<FullMovieCollection?>> get() = _collectionMoviesState

    private val _moviesImagesState =
        MutableStateFlow<UiState<ImagesResponseTMDB?>>(UiState.Loading)
    val moviesImagesState: StateFlow<UiState<ImagesResponseTMDB?>> get() = _moviesImagesState

    init {
        _movieState.value = UiState.Loading
    }


    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val movieDetails = movieRepo.getMovieDetails(movieId)
                if (movieDetails.isEmpty()) {
                    _movieState.update { UiState.Error(Exception("Movie details not found")) }
                    return@launch
                } else {
                    _movieState.update { UiState.Success(movieDetails) }
                    if (movieDetails.movieCollection != null) {
                        getCollectionMovies(collectionId = movieDetails.movieCollection.id)

                    }
                }
            } catch (e: Exception) {
                _movieState.value = UiState.Error(e)
                Log.d("DetailsViewModel", "getMovieDetails: ", e)
            }
        }
    }

    private suspend fun getCollectionMovies(collectionId: Int) {
        try {
            val movieCollection = movieRepo.getCollection(collectionId = collectionId)
            val movieItems =
                movieCollection?.parts?.map { it.convertToMovieDetailsItem() } ?: emptyList()
            _collectionMoviesState.update {
                UiState.Success(
                    FullMovieCollection(
                        id = collectionId,
                        name = movieCollection?.name ?: "",
                        overview = movieCollection?.overview ?: "",
                        movies = movieItems
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("DetailsViewModel", "getMovieCast: ", e)
        }

    }


    fun getMovieCast(movieId: Int) {
        viewModelScope.launch {
            try {
                val movieDetails = movieRepo.getMovieActors(movieId)
                _movieCastState.value = UiState.Success(movieDetails)
            } catch (e: Exception) {
                Log.e("DetailsViewModel", "getMovieCast: ", e)
            }
        }
    }


    fun getSimilarMovies(movieId: Int) {
        viewModelScope.launch {
            try {
                val movieDetails = movieRepo.getSimilarMovies(movieId)
                _similarMoviesState.value = UiState.Success(movieDetails)
            } catch (e: Exception) {
                Log.e("DetailsViewModel", "getSimilarMovies: ", e)
            }
        }
    }

    fun getMovieImages(movieId: Int) {
        viewModelScope.launch {
            try {
                val movieDetails = movieRepo.getMovieImages(movieId = movieId)
                Log.d("DetailsViewModel", "getMovieImages: $movieDetails")
                _moviesImagesState.value = UiState.Success(data = movieDetails)
            } catch (e: Exception) {
                Log.e("DetailsViewModel", "getSimilarMovies: ", e)
            }
        }
    }
}
