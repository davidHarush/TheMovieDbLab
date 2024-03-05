package com.david.movie.notwork

import com.david.movie.notwork.dto.MovieCreditsList
import com.david.movie.notwork.dto.MovieDetailsTMDB
import com.david.movie.notwork.dto.MovieList

import com.david.movie.notwork.dto.PersonExternalIdsTMDB
import com.david.movie.notwork.dto.PersonMovieCredits
import com.david.movie.notwork.dto.PersonTMDB
import com.david.movie.notwork.dto.PopularPersonList
import com.david.movie.notwork.dto.SimilarMoviesList
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json


class Movie(
    private val client: HttpClient,
    private val json: Json
) : IMovie {

    private fun handleException(e: Exception) {
        print( e.message)

    }

    override suspend fun getPopular(page: Int): MovieList? {
        return getMovies(HttpRoutes.Movies.popular(page))
    }

    override suspend fun getTopRated(page: Int): MovieList? {
        return getMovies(HttpRoutes.Movies.topRated(page))
    }


    private suspend fun getMovies(url : String): MovieList? {
        return try {
            val movieResult: String = client.get {
                url(url)
            }
            json.decodeFromString(string = movieResult , deserializer = MovieList.serializer())
        } catch (e: Exception) {
            handleException(e)
            null
        }

    }

    override suspend fun getMovieDetails(id: Int): MovieDetailsTMDB? {
        return try {
            val url = HttpRoutes.Movies.details(id)
            client.get {
                url(url)
            }
        } catch (e: Exception) {
            handleException(e)
            null
        }
    }

    override suspend fun getSimilarMovies(id: Int): SimilarMoviesList? {
        return try {
            val url = HttpRoutes.Movies.similar(id)
            client.get {
                url(url)
            }
        } catch (e: Exception) {
            handleException(e)
            null
        }
    }


    override suspend fun getMovieCredits(id: Int): MovieCreditsList? {
        return try {

            val url = HttpRoutes.Movies.credits(id)
            client.get {
                url(url)
            }
        } catch (e: Exception) {
            handleException(e)
            null
        }
    }



}


class Person(
    private val client: HttpClient,
    private val json: Json
) : IPerson {

    private fun handleException(e: Exception) {
        print( e.message)

    }

    override suspend fun getPersonDetails(personId: Int): PersonTMDB? {
        return try {

            val url = HttpRoutes.Person.details(personId)
            client.get {
                url(url)
            }
        } catch (e: Exception) {
            handleException(e)
            null
        }
    }

    override suspend fun getPersonIds(personId: Int): PersonExternalIdsTMDB? {
        return try {

            val url = HttpRoutes.Person.externalIds(personId)
            client.get {
                url(url)
            }
        } catch (e: Exception) {
            handleException(e)
            null
        }
    }

    override suspend fun getPersonMovieCredits(personId: Int): PersonMovieCredits? {
        return try {

            val url = HttpRoutes.Person.movieCredits(personId)
            client.get {
                url(url)
            }
        } catch (e: Exception) {
            handleException(e)
            null
        }
    }

    override suspend fun getPopular(page: Int): PopularPersonList? {

        return try {
            val url = HttpRoutes.Person.popular(page = page)
            client.get {
                url(url)
            }
        } catch (e: Exception) {
            handleException(e)
            null
        }
    }

}

