package com.david.movie.notwork.dto

import kotlinx.serialization.Serializable


@Serializable
data class PersonMovieCredits(
    val id: Int,
    val cast: List<MovieCastCreditTMDB>,
    val crew: List<MovieCrewCreditTMDB>
)

@Serializable
data class MovieCastCreditTMDB(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String?,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val character: String,
    val credit_id: String,
    val order: Int
)

@Serializable
data class MovieCrewCreditTMDB(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String?,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val department: String,
    val job: String,
    val credit_id: String
)
