package com.david.movie.notwork.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieList(
    val page: Int,
    val results: List<MovieItemTMDB>
)

@Serializable
data class MovieItemTMDB(
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
data class MovieDetailsTMDB(
    val adult: Boolean,
    val backdrop_path: String?,
    val belongs_to_collection: CollectionTMDB?,
    val budget: Long,
    val genres: List<GenreTMDB>,
    val homepage: String?,
    val id: Long,
    val imdb_id: String?,
    val original_language: String,
    val original_title: String,
    val overview: String?,
    val popularity: Double,
    val poster_path: String?,
    val production_companies: List<ProductionCompanyTMDB>,
    val production_countries: List<ProductionCountryTMDB>,
    val release_date: String,
    val revenue: Long,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguageTMDB>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

@Serializable
data class CollectionTMDB(
    val id: Long,
    val name: String,
    val poster_path: String?,
    val backdrop_path: String?
)

@Serializable
data class GenreTMDB(
    val id: Int,
    val name: String
)

@Serializable
data class ProductionCompanyTMDB(
    val id: Int,
    val logo_path: String?,
    val name: String,
    val origin_country: String
)

@Serializable
data class ProductionCountryTMDB(
    val iso_3166_1: String,
    val name: String
)

@Serializable
data class SpokenLanguageTMDB(
    val english_name: String,
    val iso_639_1: String,
    val name: String
)


@Serializable
data class Genre(
    val id: Int,
    val name: String
)

@Serializable
data class Genres(
    val genres: List<Genre>
)

