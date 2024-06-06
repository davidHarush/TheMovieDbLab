package com.david.movie.movielab.ui.screens.favorite

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.david.movie.movielab.BaseViewModel
import com.david.movie.movielab.db.MovieDBItem
import com.david.movie.movielab.db.MovieDao
import com.david.movie.movielab.repo.model.MovieDetailsItem
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.movielab.repo.model.toDbItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val movieDao: MovieDao) : BaseViewModel() {

    val moviesAsFlow = movieDao.getAllMoviesAsFlow()

    fun <T> onFavoriteClick(movie: T, isFavorite: Boolean, lastWatch: String? = null) {
        if (movie is MovieItem) {
            val movieDb = MovieDBItem(
                id = movie.id,
                title = movie.title,
                overview = movie.overview,
                releaseDate = movie.release_date,
                posterPath = movie.poster_path,
                backdropPath = movie.backdrop_path,
                isFavorite = isFavorite,
                lastWatch = lastWatch
            )
            insertOrDeleteMovie(movie.toDbItem(), isFavorite)
        }
        if (movie is MovieDetailsItem) {
            val movieDb = MovieDBItem(
                id = movie.id,
                title = movie.title,
                overview = movie.overview,
                releaseDate = movie.release_date,
                posterPath = movie.poster_path,
                backdropPath = movie.backdrop_path,
                isFavorite = isFavorite,
                lastWatch = lastWatch
            )
            insertOrDeleteMovie(movieDb, isFavorite)
        }
    }

    private fun insertOrDeleteMovie(movie: MovieDBItem, isFavorite: Boolean) {
        viewModelScope.launch {
            Log.d("FavoriteButtonViewModel", "movieDb: $movie")


            if (movieDao.isMovieExists(movie.id)) {
                movieDao.deleteMovieById(movie.id)
            } else {
                movieDao.insertOrReplaceMovie(movie)
            }

        }
    }


    fun deleteAllMovies() {
        viewModelScope.launch {
            movieDao.deleteAllMovies()
        }
    }

    suspend fun isMovieInFavorite(id: Int): Boolean {
        val movie: MovieDBItem? = movieDao.getMovieById(id)
        return movie?.isFavorite ?: false
    }

    suspend fun getFavoriteStatus(movieId: Int): Boolean {
        return movieDao.isMovieExists(movieId)
    }

}