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
import io.ktor.client.engine.android.Android
import io.ktor.client.features.BodyProgress
import io.ktor.client.features.cache.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
object TMDBService {
    @OptIn(ExperimentalSerializationApi::class)
    private val json by lazy {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = true
            isLenient = true
            prettyPrint = true
        }
    }

    private val client by lazy {
        HttpClient(Android) {

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("TMDBService - call :$message")
                    }
                }
               // logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
            install(HttpCache)
        }
    }

    val person: IPerson by lazy { Person(client, json) }
    val movie: IMovie by lazy { Movie(client, json) }
}



interface IMovie {
    // Movies
    suspend fun getMovieCredits(id: Int): MovieCreditsList?
    suspend fun getMovieDetails(id: Int): MovieDetailsTMDB?
    suspend fun getSimilarMovies(id: Int): SimilarMoviesList?
    suspend fun getPopular(page: Int = 1): MovieList?
    suspend fun getTopRated(page: Int = 1): MovieList?

    // People

}

interface IPerson {
    suspend fun getPersonDetails(personId: Int): PersonTMDB?
    suspend fun getPersonIds(personId: Int): PersonExternalIdsTMDB?
    suspend fun getPersonMovieCredits(personId: Int): PersonMovieCredits?
    suspend fun getPopular(page: Int = 1): PopularPersonList?

}