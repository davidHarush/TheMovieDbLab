package com.david.movie.notwork

import com.david.movie.notwork.dto.Genres
import com.david.movie.notwork.dto.ImagesResponseTMDB
import com.david.movie.notwork.dto.MovieCollectionTMDB
import com.david.movie.notwork.dto.MovieCreditsList
import com.david.movie.notwork.dto.MovieDetailsTMDB
import com.david.movie.notwork.dto.MovieList
import com.david.movie.notwork.dto.PersonExternalIdsTMDB
import com.david.movie.notwork.dto.PersonMovieCredits
import com.david.movie.notwork.dto.PersonTMDB
import com.david.movie.notwork.dto.PopularPersonList
import com.david.movie.notwork.dto.SimilarMoviesList
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.cache.HttpCache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
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

    val person: IPersonService by lazy { PersonService(client, json) }
    val movie: IMovieService by lazy { MovieService(client, json) }
}


interface IMovieService {
    // Movies
    suspend fun getMovieCredits(id: Int): MovieCreditsList?
    suspend fun getMovieDetails(id: Int): MovieDetailsTMDB?
    suspend fun getSimilarMovies(id: Int): SimilarMoviesList?
    suspend fun getPopular(page: Int = 1): MovieList?
    suspend fun getNowPlaying(page: Int = 1): MovieList?
    suspend fun getTopRated(page: Int = 1): MovieList?
    suspend fun getGenres(): Genres?

    suspend fun discoverMovies(
        page: Int = 1,
        withGenres: List<Int> = emptyList(),
        rating: Float = 7f
    ): MovieList?

    suspend fun search(query: String, page: Int): MovieList?

    suspend fun getImages(id: Int): ImagesResponseTMDB?
    suspend fun getCollection(id: Int): MovieCollectionTMDB?
}

interface IPersonService {
    suspend fun getPersonDetails(personId: Int): PersonTMDB?
    suspend fun getPersonIds(personId: Int): PersonExternalIdsTMDB?
    suspend fun getPersonMovieCredits(personId: Int): PersonMovieCredits?
    suspend fun getPopular(page: Int = 1): PopularPersonList?
    suspend fun search(query: String, page: Int): PopularPersonList?


}