package com.david.movie.notwork.dto

import kotlinx.serialization.Serializable


@Serializable
data class MovieCreditsList(
    val id: Int,
    val cast: List<CastMemberTMDB>,
    val crew: List<CrewMemberTMDB>
)

@Serializable
data class CastMemberTMDB(
    val adult: Boolean?,
    val gender: Int?,
    val id: Int?,
    val known_for_department: String?,
    val name: String?,
    val original_name: String?,
    val popularity: Double?,
    val profile_path: String?,
    val cast_id: Int?,
    val character: String?,
    val credit_id: String?,
    val order: Int?,
)

@Serializable
data class CrewMemberTMDB(
    val adult: Boolean?,
    val gender: Int?,
    val id: Int?,
    val known_for_department: String?,
    val name: String?,
    val original_name: String?,
    val popularity: Double?,
    val profile_path: String?,
    val credit_id: String?,
    var department: String?,
    var job: String?
)


fun CastMemberTMDB.getImageUrl() =
    "https://image.tmdb.org/t/p/w500/$profile_path"

fun CastMemberTMDB.isActor() = known_for_department == "Acting"



