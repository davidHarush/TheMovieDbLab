package com.david.movie.notwork

import com.david.movie.notwork.dto.MovieCreditsList
import com.david.movie.notwork.dto.MovieDetails
import com.david.movie.notwork.dto.MovieList
import com.david.movie.notwork.dto.Person
import com.david.movie.notwork.dto.PersonExternalIds
import com.david.movie.notwork.dto.PersonMovieCredits
import com.david.movie.notwork.dto.SimilarMoviesList
import kotlinx.serialization.builtins.serializer
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer


class MovieServiceImpl(
    private val client: HttpClient,
    private val json: Json
) : IMovieService {

    private fun handleException(e: Exception) {
        print( e.message)

    }

    override suspend fun getPopular(page: Int): MovieList? {
        print("getPopular")
        return getMovies(HttpRoutes.Movies.popular(page))
    }

    override suspend fun getTopRated(page: Int): MovieList? {
        print("getTopRated")
        return getMovies(HttpRoutes.Movies.topRated(page))
    }


    private suspend fun getMovies(url : String): MovieList? {
        print("XXXXX getMovies : url $url")

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

    override suspend fun getMovieDetails(id: Int): MovieDetails? {
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


    override suspend fun getPersonDetails(personId: Int): Person? {
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

    override suspend fun getPersonIds(personId: Int): PersonExternalIds? {
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

}
