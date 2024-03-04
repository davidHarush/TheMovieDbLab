package com.david.movie.notwork

import com.david.movie.notwork.dto.MovieCreditsList
import com.david.movie.notwork.dto.MovieDetails
import com.david.movie.notwork.dto.MovieList
import com.david.movie.notwork.dto.Person
import com.david.movie.notwork.dto.PersonExternalIds
import com.david.movie.notwork.dto.PersonMovieCredits
import com.david.movie.notwork.dto.SimilarMoviesList
import io.ktor.client.*
import io.ktor.client.engine.android.Android
import io.ktor.client.features.cache.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json


interface IMovieService {

    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        fun create(): IMovieService {

            val json = Json {
                ignoreUnknownKeys = true
                explicitNulls = true
                isLenient = true
                prettyPrint = true
            }

            return MovieServiceImpl(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(JsonFeature) {
                        serializer = KotlinxSerializer(json = json)
                    }
                    install(HttpCache)
                },
                json = json

            )
        }
    }

    // Movies
    suspend fun getMovieCredits(id: Int): MovieCreditsList?
    suspend fun getMovieDetails(id: Int): MovieDetails?
    suspend fun getSimilarMovies(id: Int): SimilarMoviesList?
    suspend fun getPopular(page: Int = 1): MovieList?
    suspend fun getTopRated(page: Int = 1): MovieList?


    // People
    suspend fun getPersonDetails(personId: Int): Person?
    suspend fun getPersonIds(personId: Int): PersonExternalIds?
    suspend fun getPersonMovieCredits(personId: Int): PersonMovieCredits?
}
