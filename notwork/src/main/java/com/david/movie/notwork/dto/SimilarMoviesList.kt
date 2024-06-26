package com.david.movie.notwork.dto

import kotlinx.serialization.Serializable

@Serializable
data class SimilarMoviesList(
    val page: Int,
    val results: List<SimilarMovieTMDB>,
    val total_pages: Int,
    val total_results: Int
)

@Serializable
data class SimilarMovieTMDB(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

fun SimilarMovieTMDB.getPosterUrl() = "https://image.tmdb.org/t/p/w500/$poster_path"
fun SimilarMovieTMDB.getBackdropUrl() = "https://image.tmdb.org/t/p/w500/$backdrop_path"
fun SimilarMovieTMDB.isValid() = !poster_path.isNullOrEmpty() && !backdrop_path.isNullOrEmpty()

