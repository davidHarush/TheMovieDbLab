package com.david.movie.notwork.dto

import kotlinx.serialization.Serializable


@Serializable
data class Person(
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
data class PersonExternalIds(
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
        fun getEmpty(): PersonExternalIds = PersonExternalIds(
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
