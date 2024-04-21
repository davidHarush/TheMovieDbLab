package com.david.movie.movielab.repo

import android.util.Log
import com.david.movie.movielab.repo.Mapper.mapMovieCastCreditToMovieItems
import com.david.movie.movielab.repo.model.MovieItem
import com.david.movie.notwork.IPersonService
import com.david.movie.notwork.TMDBService
import com.david.movie.notwork.dto.PersonExternalIdsTMDB
import com.david.movie.notwork.dto.PersonTMDB
import com.david.movie.notwork.dto.PopularPersonList
import javax.inject.Inject


class PersonRepo @Inject constructor() {

    private val personService: IPersonService get() = TMDBService.person


    /**
     * get actors movie credits for a given actor  (only for cast members who are actors)
     */
    suspend fun getPersonMovies(personId: Int): List<MovieItem> =
        try {
            val response = personService.getPersonMovieCredits(personId = personId)
            mapMovieCastCreditToMovieItems(response?.cast ?: emptyList())
        } catch (e: Exception) {
            Log.e("MovieRepo", "getPopularPerson: $e")
            emptyList()
        }

    /**
     * get person details for a given person
     */
    suspend fun getPersonDetails(personId: Int): PersonTMDB? =
        try {
            personService.getPersonDetails(personId)
        } catch (e: Exception) {
            Log.e("MovieRepo", "getPopularPerson: $e")
            null
        }

    /**
     * get person external ids for a given person
     */
    suspend fun getPersonIds(personId: Int): PersonExternalIdsTMDB? =
        try {
            personService.getPersonIds(personId)
        } catch (e: Exception) {
            Log.e("MovieRepo", "getPopularPerson: $e")
            null
        }

    suspend fun searchPersons(query: String, page: Int = 1): PopularPersonList? =
        try {
            personService.search(query = query, page = page)
        } catch (e: Exception) {
            Log.e("MovieRepo", "searchMovies: $e")
            null
        }


}

