package com.david.movie.notwork.dto

import kotlinx.serialization.Serializable

@Serializable
data class BackdropTMDB(
    val aspect_ratio: Double,
    val height: Int,
    val iso_639_1: String?,
    val file_path: String,
    val vote_average: Double,
    val vote_count: Int,
    val width: Int
)

@Serializable
data class LogoTMDB(
    val aspect_ratio: Double,
    val height: Int,
    val iso_639_1: String?,
    val file_path: String,
    val vote_average: Double,
    val vote_count: Int,
    val width: Int
)

@Serializable
data class PosterTMDB(
    val aspect_ratio: Double,
    val height: Int,
    val iso_639_1: String?,
    val file_path: String,
    val vote_average: Double,
    val vote_count: Int,
    val width: Int
)

@Serializable
data class ImagesResponseTMDB(
    val backdrops: List<BackdropTMDB>,
    val logos: List<LogoTMDB>,
    val posters: List<PosterTMDB>,
    val id: Int
)


fun PosterTMDB.getFullImageUrl() = "https://image.tmdb.org/t/p/w500/$file_path"
fun LogoTMDB.getFullImageUrl() = "https://image.tmdb.org/t/p/w500/$file_path"
fun BackdropTMDB.getFullImageUrl() = "https://image.tmdb.org/t/p/w500/$file_path"