package com.david.movie.notwork.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class MovieList(
    val page: Int,
    val results: List<MovieItem>
)

@Serializable
data class MovieItem(
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

@Serializable
data class MovieDetails(
    val adult: Boolean,
    val backdrop_path: String?,
    val belongs_to_collection: Collection?,
    val budget: Long,
    val genres: List<Genre>,
    val homepage: String?,
    val id: Long,
    val imdb_id: String?,
    val original_language: String,
    val original_title: String,
    val overview: String?,
    val popularity: Double,
    val poster_path: String?,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Long,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

@Serializable
data class Collection(
    val id: Long,
    val name: String,
    val poster_path: String?,
    val backdrop_path: String?
)

@Serializable
data class Genre(
    val id: Int,
    val name: String
)

@Serializable
data class ProductionCompany(
    val id: Int,
    val logo_path: String?,
    val name: String,
    val origin_country: String
)

@Serializable
data class ProductionCountry(
    val iso_3166_1: String,
    val name: String
)

@Serializable
data class SpokenLanguage(
    val english_name: String,
    val iso_639_1: String,
    val name: String
)


