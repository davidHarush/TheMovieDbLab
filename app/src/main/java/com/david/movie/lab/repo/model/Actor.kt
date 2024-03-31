package com.david.movie.lab.repo.model

import com.david.movie.notwork.dto.PopularPersonTMDB

data class Actor(
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

    ) {
    constructor(person: PopularPersonTMDB) : this(
        gender = person.gender,
        id = person.id,
        known_for_department = person.known_for_department,
        name = person.name,
        original_name = person.name,
        popularity = person.popularity,
        profile_path = person.profile_path,
        cast_id = null,
        character = null,
        credit_id = null,
        order = null
    )
}

fun Actor.getProfileUrl() =
    "https://image.tmdb.org/t/p/w500/$profile_path"

