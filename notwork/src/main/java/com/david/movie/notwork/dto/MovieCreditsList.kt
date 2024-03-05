package com.david.movie.notwork.dto

import kotlinx.serialization.Serializable


@Serializable
data class MovieCreditsList(
    val id: Int,
    val cast: List<CastMember>,
    val crew: List<CrewMember>
)

@Serializable
data class CastMember(
    val adult : Boolean?,
    val gender : Int?,
    val id: Int?,
    val known_for_department : String?,
    val name : String?,
    val original_name : String?,
    val popularity : Double?,
    val profile_path : String?,
    val cast_id : Int?,
    val character : String?,
    val credit_id : String?,
    val order : Int?,
)

@Serializable
data class CrewMember(
    val adult : Boolean?,
    val gender : Int?,
    val id: Int?,
    val known_for_department : String?,
    val name : String?,
    val original_name : String?,
    val popularity : Double?,
    val profile_path : String?,
    val credit_id : String?,
    var department : String?,
    var job : String?
)




fun CastMember.getImageUrl() =
    "https://image.tmdb.org/t/p/w500/$profile_path"

fun  CastMember.isActor() = known_for_department == "Acting"



