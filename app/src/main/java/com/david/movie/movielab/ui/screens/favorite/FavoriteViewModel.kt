package com.david.movie.movielab.ui.screens.favorite

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.david.movie.movielab.BaseViewModel
import com.david.movie.movielab.db.MovieDBItem
import com.david.movie.movielab.db.MovieDao
import com.david.movie.movielab.repo.model.MovieItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val movieDao: MovieDao) : BaseViewModel() {

    val moviesAsFlow = movieDao.getAllMoviesAsFlow()

    fun <T> onFavoriteClick(movie: T, isFavorite: Boolean) {
        if (movie is MovieItem) {
            insertOrDeleteMovie(movie)
        }
    }

    private fun insertOrDeleteMovie(movie: MovieItem) {
        viewModelScope.launch {
            val movieDb = MovieDBItem(
                id = movie.id,
                title = movie.title,
                overview = movie.overview,
                releaseDate = movie.release_date,
                posterPath = movie.poster_path,
                backdropPath = movie.backdrop_path,



            )
            Log.d("FavoriteButtonViewModel", "movieDb: $movieDb")


            if (movieDao.isMovieExists(movie.id)) {
                movieDao.deleteMovieById(movie.id)
            } else {
                movieDao.insertOrReplaceMovie(movieDb)
            }

        }

    }

}