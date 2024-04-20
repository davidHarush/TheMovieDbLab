package com.david.movie.movielab.repo

import android.util.Log
import com.david.movie.movielab.repo.model.Actor
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.TMDBService
import com.david.movie.notwork.dto.CastMemberTMDB
import com.david.movie.notwork.dto.MovieList
import com.david.movie.notwork.dto.PersonExternalIdsTMDB
import com.david.movie.notwork.dto.PersonTMDB
import com.david.movie.notwork.dto.PopularPersonList
import com.david.movie.notwork.dto.isValid
import com.david.movie.notwork.dto.isValidActor
import javax.inject.Inject


class PersonRepo @Inject constructor() {


    /**
     * get actors movie credits for a given actor  (only for cast members who are actors)
     */
    suspend fun getPersonMovies(personId: Int): List<MovieItem>? {
        val response = TMDBService.person.getPersonMovieCredits(personId = personId)
        return response?.cast
            ?.filter { movie -> movie.isValid() }
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
     * get person details for a given person
     */
    suspend fun getPersonDetails(personId: Int): PersonTMDB? =
        try {
            TMDBService.person.getPersonDetails(personId)
        } catch (e: Exception) {
            Log.e("MovieRepo", "getPopularPerson: $e")
            null
        }

    /**
     * get person external ids for a given person
     */
    suspend fun getPersonIds(personId: Int): PersonExternalIdsTMDB? =
        try {
            TMDBService.person.getPersonIds(personId)
        } catch (e: Exception) {
            Log.e("MovieRepo", "getPopularPerson: $e")
            null
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
                voteAverage = movie.vote_average
            )
        } ?: emptyList()
    }


    private fun getActorsFromCast(castMembers: List<CastMemberTMDB>): List<Actor> {
        // Filter out only those cast members who are actors and then map each CastMember to an Actor
        return castMembers.filter { it.isValidActor() }
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

    suspend fun searchPersons(query: String, page: Int = 1): PopularPersonList? =
        try {
            val results = TMDBService.person.search(query = query, page = page)
            results
        } catch (e: Exception) {
            Log.e("MovieRepo", "searchMovies: $e")
            null
        }


}

