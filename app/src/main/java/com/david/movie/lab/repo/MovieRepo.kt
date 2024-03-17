package com.david.movie.lab.repo

import android.util.Log
import com.david.movie.lab.repo.model.Actor
import com.david.movie.lab.repo.model.MovieDetailsItem
import com.david.movie.lab.repo.model.MovieItem
import com.david.movie.notwork.TMDBService
import com.david.movie.notwork.dto.CastMemberTMDB
import com.david.movie.notwork.dto.Genres
import com.david.movie.notwork.dto.MovieList
import com.david.movie.notwork.dto.PersonExternalIdsTMDB
import com.david.movie.notwork.dto.PersonTMDB
import com.david.movie.notwork.dto.PopularPersonList
import com.david.movie.notwork.dto.isActor
import javax.inject.Inject

import com.david.movie.notwork.dto.MovieItemTMDB as MovieItemNetwork


class MovieRepo @Inject constructor() {

    suspend fun getPopularMovieList(page: Int = 1): List<MovieItem> {
        val response = TMDBService.movie.getPopular(page)
        Log.d("MovieRepo", "getPopularMovieList: $response")
        return convertMovieList(response?.results)
    }

    /**
     * get top rated movies
     */
    suspend fun getTopRatedMovieList(page: Int = 1): List<MovieItem> {
        val response = TMDBService.movie.getTopRated(page)
        return convertMovieList(response?.results)
    }


    /**
     * get similar movies to a given movie
     */
    suspend fun getSimilarMovies(movieId: Int): List<MovieItem>? {
        val response = TMDBService.movie.getSimilarMovies(movieId)
        return response?.results
            ?.filter { similarMovie ->
                !similarMovie.poster_path.isNullOrEmpty() && !similarMovie.backdrop_path.isNullOrEmpty()
            }
            ?.map { similarMovie ->
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
        val response = TMDBService.person.getPersonMovieCredits(personId = personId)
        return response?.cast
            ?.filter { similarMovie ->
                !similarMovie.poster_path.isNullOrEmpty() && !similarMovie.backdrop_path.isNullOrEmpty()
            }
            ?.map { castCredit ->
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
        val response = TMDBService.movie.getMovieDetails(movieId)
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
        val response = TMDBService.movie.getMovieCredits(movieId)
        return getActorsFromCast(castMembers = response?.cast ?: emptyList())
    }


    /**
     * get person details for a given person
     */
    suspend fun getPersonDetails(personId: Int): PersonTMDB? {
        return TMDBService.person.getPersonDetails(personId)
    }


    /**
     * get person external ids for a given person
     */
    suspend fun getPersonIds(personId: Int): PersonExternalIdsTMDB? {
        return TMDBService.person.getPersonIds(personId)
    }

    suspend fun getPopularPerson(page: Int  = 1): PopularPersonList?  =
          try {
            val result =  TMDBService.person.getPopular(page = page)
            result
        } catch (e: Exception) {
            Log.e("MovieRepo", "getPopularPerson: $e")
            null
        }



    suspend fun getMovieGenres(): Genres?  =
        try {
            TMDBService.movie.getGenres()
        } catch (e: Exception) {
            Log.e("MovieRepo", "getPopularPerson: $e")
            null
        }



    suspend fun discoverMovies(genreList: List<Int>): List<MovieItem> {
        try {
            val movieList = TMDBService.movie.discoverMovies(withGenres =  genreList)
            return convertToMovieItemList(movieList)
        } catch (e: Exception) {
            Log.e("MovieRepo", "getPopularPerson: $e")
        }
        return emptyList()
    }

    private fun convertToMovieItemList(movieList: MovieList?): List<MovieItem> {

        return movieList?.results?.map { movie ->
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


    private fun getActorsFromCast(castMembers: List<CastMemberTMDB>): List<Actor> {
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

    suspend fun searchMovies(query: String): List<MovieItem> =
        try {
            val movieList = TMDBService.movie.search(query = query, page = 1)
            convertToMovieItemList(movieList)
        } catch (e: Exception) {
            Log.e("MovieRepo", "searchMovies: $e")
            emptyList<MovieItem>()
        }



    suspend fun searchPersons(query: String): PopularPersonList?  =
        try {
            val results = TMDBService.person.search(query = query, page = 1)
            results
        } catch (e: Exception) {
            Log.e("MovieRepo", "searchMovies: $e")
            null
        }


}

