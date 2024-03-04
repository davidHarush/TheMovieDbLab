package com.david.movie.lab.repo

import android.util.Log
import com.david.movie.lab.repo.model.Actor
import com.david.movie.lab.repo.model.MovieDetailsItem
import com.david.movie.lab.repo.model.MovieItem
import com.david.movie.notwork.IMovieService
import com.david.movie.notwork.dto.CastMember
import com.david.movie.notwork.dto.Person
import com.david.movie.notwork.dto.PersonExternalIds
import com.david.movie.notwork.dto.isActor
import javax.inject.Inject

import com.david.movie.notwork.dto.MovieItem as MovieItemNetwork


class MovieRepo @Inject constructor() {

    private val movieService = IMovieService.create()


    /**
     * get popular movies
     */


    /**
     * get popular movies
     */
    suspend fun getPopularMovieList(page: Int = 1): List<MovieItem> {
        val response = movieService.getPopular(page)
        Log.d("MovieRepo", "getPopularMovieList: $response")
        return convertMovieList(response?.results)
    }

    /**
     * get top rated movies
     */
    suspend fun getTopRatedMovieList(page: Int = 1): List<MovieItem> {
        val response = movieService.getTopRated(page)
        return convertMovieList(response?.results)
    }


    /**
     * get similar movies to a given movie
     */
    suspend fun getSimilarMovies(movieId: Int): List<MovieItem>? {
        val response = movieService.getSimilarMovies(movieId)
        return response?.results?.map { similarMovie ->
            MovieItem(
                backdrop_path = similarMovie.backdrop_path,
                id = similarMovie.id,
                original_language = similarMovie.original_language,
                original_title = similarMovie.original_title,
                overview = similarMovie.overview,
                poster_path = similarMovie.poster_path,
                release_date = similarMovie.release_date,
                title = similarMovie.title,
                video = similarMovie.video,
                voteAverage = similarMovie.vote_average
            )
        }
    }

    /**
     * get actors movie credits for a given actor  (only for cast members who are actors)
     */
    suspend fun getPersonMovies(personId: Int): List<MovieItem>? {
        val response = movieService.getPersonMovieCredits(personId = personId)
        return response?.cast?.map { castCredit ->
            MovieItem(
                backdrop_path = castCredit.backdrop_path,
                id = castCredit.id,
                original_language = castCredit.original_language,
                original_title = castCredit.original_title,
                overview = castCredit.overview,
                poster_path = castCredit.poster_path,
                release_date = castCredit.release_date ?: "", // Handle nullable release_date
                title = castCredit.title,
                video = castCredit.video,
                voteAverage = castCredit.vote_average
            )
        }
    }

    /**
     * get movie details for a given movie
     */
    suspend fun getMovieDetails(movieId: Int): MovieDetailsItem {
        val response = movieService.getMovieDetails(movieId)
        return MovieDetailsItem(
            backdrop_path = response?.backdrop_path,
            id = response?.id?.toInt() ?: 0,
            original_language = response?.original_language ?: "",
            original_title = response?.original_title ?: "",
            overview = response?.overview ?: "",
            poster_path = response?.poster_path,
            release_date = response?.release_date ?: "",
            title = response?.title ?: "",
            video = response?.video ?: false,
            tagline = response?.tagline ?: "",
            voteAverage = (response?.vote_average ?: 0) as Double,
            genres = response?.genres ?: emptyList()
        )
    }

    private fun convertMovieList(results: List<MovieItemNetwork>?): List<MovieItem> {
        return results?.map { movie ->
            MovieItem(
                backdrop_path = movie.backdrop_path,
                id = movie.id,
                original_language = movie.original_language,
                original_title = movie.original_title,
                overview = movie.overview,
                poster_path = movie.poster_path,
                release_date = movie.release_date,
                title = movie.title,
                video = movie.video,
                voteAverage = movie.vote_average ?: 0.0
            )
        } ?: emptyList()
    }


    /**
     * get movie actors for a given movie
     */
    suspend fun getMovieActors(movieId: Int): List<Actor> {
        val response = movieService.getMovieCredits(movieId)
        return getActorsFromCast(castMembers = response?.cast ?: emptyList())
    }


    /**
     * get person details for a given person
     */
    suspend fun getPersonDetails(personId: Int): Person? {
        return movieService.getPersonDetails(personId)
    }


    /**
     * get person external ids for a given person
     */
    suspend fun getPersonIds(personId: Int): PersonExternalIds? {
        return movieService.getPersonIds(personId)
    }


    private fun getActorsFromCast(castMembers: List<CastMember>): List<Actor> {
        // Filter out only those cast members who are actors and then map each CastMember to an Actor
        return castMembers.filter { it.isActor() && it.profile_path?.isNotEmpty() ?: false }
            .map { castMember ->
                Actor(
                    gender = castMember.gender,
                    id = castMember.id,
                    known_for_department = castMember.known_for_department,
                    name = castMember.name,
                    original_name = castMember.original_name,
                    popularity = castMember.popularity,
                    profile_path = castMember.profile_path,
                    cast_id = castMember.cast_id,
                    character = castMember.character,
                    credit_id = castMember.credit_id,
                    order = castMember.order
                )
            }
    }

}