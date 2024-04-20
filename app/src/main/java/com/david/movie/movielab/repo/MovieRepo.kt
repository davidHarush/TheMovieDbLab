package com.david.movie.movielab.repo

import android.util.Log
import com.david.movie.movielab.repo.Mapper.convertToMovieDetailsItem
import com.david.movie.movielab.repo.Mapper.mapTMDBCastMembersToActors
import com.david.movie.movielab.repo.Mapper.mapTMDBMovieItemsToMovieItems
import com.david.movie.movielab.repo.Mapper.mapTMDBMovieListToMovieItemList
import com.david.movie.movielab.repo.Mapper.mapTMDBSimilarMoviesToMovieItems
import com.david.movie.movielab.repo.model.Actor
import com.david.movie.movielab.repo.model.MovieDetailsItem
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.TMDBService
import com.david.movie.notwork.dto.Genres
import javax.inject.Inject


class MovieRepo @Inject constructor() {

    suspend fun getPopularMovieList(page: Int = 1): List<MovieItem> {
        val response = TMDBService.movie.getPopular(page)
        Log.d("MovieRepo", "getPopularMovieList: $response")
        return mapTMDBMovieItemsToMovieItems(response?.results)
    }

    suspend fun getTopRatedMovieList(page: Int = 1): List<MovieItem> {
        val response = TMDBService.movie.getTopRated(page)
        return mapTMDBMovieItemsToMovieItems(TMDBService.movie.getTopRated(page)?.results)
    }


    /**
     * get similar movies to a given movie
     */
    suspend fun getSimilarMovies(movieId: Int): List<MovieItem>? {
        val response = TMDBService.movie.getSimilarMovies(movieId)
        return mapTMDBSimilarMoviesToMovieItems(response?.results)
    }

    /**
     * get movie details for a given movie
     */
    suspend fun getMovieDetails(movieId: Int): MovieDetailsItem {
        return try {
            val response = TMDBService.movie.getMovieDetails(movieId)
            response?.convertToMovieDetailsItem() ?: MovieDetailsItem.getEmpty()
        } catch (e: Exception) {
            Log.e("MovieRepo", "getPopularPerson: $e")
            MovieDetailsItem.getEmpty()
        }

    }

    /**
     * get movie actors for a given movie
     */
    suspend fun getMovieActors(movieId: Int): List<Actor> {
        val response = TMDBService.movie.getMovieCredits(movieId)
        return mapTMDBCastMembersToActors(castMembers = response?.cast ?: emptyList())
    }


    suspend fun getMovieGenres(): Genres? =
        try {
            TMDBService.movie.getGenres()
        } catch (e: Exception) {
            Log.e("MovieRepo", "getPopularPerson: $e")
            null
        }


    suspend fun searchMovies(query: String): List<MovieItem> =
        try {
            mapTMDBMovieListToMovieItemList(TMDBService.movie.search(query = query, page = 1))
        } catch (e: Exception) {
            Log.e("MovieRepo", "searchMovies: $e")
            emptyList()
        }


}

