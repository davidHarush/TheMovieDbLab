package com.david.movie.movielab.repo

import android.util.Log
import com.david.movie.movielab.repo.Mapper.mapMovieCastCreditToMovieItems
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.TMDBService
import com.david.movie.notwork.dto.PersonExternalIdsTMDB
import com.david.movie.notwork.dto.PersonTMDB
import com.david.movie.notwork.dto.PopularPersonList
import javax.inject.Inject


class PersonRepo @Inject constructor() {


    /**
     * get actors movie credits for a given actor  (only for cast members who are actors)
     */
    suspend fun getPersonMovies(personId: Int): List<MovieItem> {
        val response = TMDBService.person.getPersonMovieCredits(personId = personId)
        return mapMovieCastCreditToMovieItems(response?.cast ?: emptyList())
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

    suspend fun searchPersons(query: String, page: Int = 1): PopularPersonList? =
        try {
            val results = TMDBService.person.search(query = query, page = page)
            results
        } catch (e: Exception) {
            Log.e("MovieRepo", "searchMovies: $e")
            null
        }


}

