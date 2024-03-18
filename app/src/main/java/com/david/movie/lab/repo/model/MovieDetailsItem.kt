package com.david.movie.lab.repo.model

import com.david.movie.notwork.dto.GenreTMDB
import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun MovieDetailsItem.getPosterUrl() =
    "https://image.tmdb.org/t/p/w500/$poster_path"

fun MovieDetailsItem.getBackdropUrl() =
    "https://image.tmdb.org/t/p/w500/$backdrop_path"

fun MovieDetailsItem.getReleaseYear() = getReleaseDate().year.toString()
fun MovieDetailsItem.getReleaseDate(): LocalDate =
    LocalDate.parse(release_date, DateTimeFormatter.ISO_DATE)


data class MovieDetailsItem(
    val backdrop_path: String?,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val tagline: String,
    val voteAverage: Double,
    val genres: List<GenreTMDB>,


    ) {
    override fun toString(): String {
        return "MovieItem(backdrop_path=$backdrop_path, id=$id, original_language='$original_language'" +
                ", original_title='$original_title', overview='$overview', poster_path=$poster_path, " +
                "release_date='$release_date', title='$title', video=$video)"

    }

    companion object {
        fun getEmpty(): MovieDetailsItem = MovieDetailsItem(
            backdrop_path = null,
            id = 0,
            original_language = "",
            original_title = "",
            overview = "",
            poster_path = null,
            release_date = "",
            title = "",
            video = false,
            tagline = "",
            voteAverage = 0.0,
            genres = emptyList()
        )
    }
}
