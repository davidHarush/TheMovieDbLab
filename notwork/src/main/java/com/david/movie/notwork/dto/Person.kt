package com.david.movie.notwork.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PersonTMDB(
    val adult: Boolean,
    val also_known_as: List<String>,
    val biography: String,
    val birthday: String?,
    val deathday: String?,
    val gender: Int,
    val homepage: String?,
    val id: Int,
    val imdb_id: String?,
    val known_for_department: String?,
    val name: String,
    val place_of_birth: String?,
    val popularity: Double?,
    val profile_path: String
)

@Serializable
data class PersonExternalIdsTMDB(
    val id: Int,
    val freebase_mid: String?,
    val freebase_id: String?,
    val imdb_id: String?,
    val tvrage_id: Int?,
    val wikidata_id: String?,
    val facebook_id: String?,
    val instagram_id: String?,
    val tiktok_id: String?,
    val twitter_id: String?,
    val youtube_id: String?
){


    companion object {
        fun getEmpty(): PersonExternalIdsTMDB = PersonExternalIdsTMDB(
            id = 0,
            freebase_mid = "",
            freebase_id = "",
            imdb_id = "",
            tvrage_id = 0,
            wikidata_id = "",
            facebook_id = "",
            instagram_id = "",
            tiktok_id = "",
            twitter_id = "",
            youtube_id = ""

        )
    }


}
//TMDBService - call :RESPONSE https://api.themoviedb.org/3/person/popular?api_key=56a778f90174e0061b6e7c69a5e3c9f2&language=en-US&page=1
// failed with exception: kotlinx.serialization.MissingFieldException: Fields [original_title, release_date, title, video] are required for type with serial name 'com.david.movie.notwork.dto.KnownFor', but they were missing




@Serializable
data class PopularPersonList(
    val page: Int,
    val results: List<PopularPersonTMDB>
)


@Serializable
data class PopularPersonTMDB(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val known_for: List<KnownFor> = emptyList(),
    val known_for_department: String,
    val name: String,
    val popularity: Double,
    val profile_path: String
)

@Serializable
data class KnownFor(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val id: Int,
    val media_type: String,
    val original_language: String= "",
    val original_title: String= "",
    val overview: String= "",
    val poster_path: String= "",
   val release_date: String = "",
    val title: String= "",
    val video: Boolean= false,
   val vote_average: Double,
   val vote_count: Int
)

