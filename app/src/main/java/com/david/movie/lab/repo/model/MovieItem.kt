package com.david.movie.lab.repo.model


data class MovieItem(
    val backdrop_path: String?,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
) {
    override fun toString(): String {
        return "MovieItem(backdrop_path=$backdrop_path, id=$id, original_language='$original_language'" +
                ", original_title='$original_title', overview='$overview', poster_path=$poster_path, " +
                "release_date='$release_date', title='$title', video=$video)"

    }

    companion object {
        fun createEmpty(): MovieItem = MovieItem(
            backdrop_path = null,
            id = 0,
            original_language = "",
            original_title = "",
            overview = "",
            poster_path = null,
            release_date = "",
            title = "",
            video = false,
            voteAverage = 0.0
        )

        fun isEmpty(movieItem: MovieItem) = movieItem.id == 0
    }
}


fun MovieItem.isEmpty() = MovieItem.isEmpty(this)
fun MovieItem.getPosterUrl() =
    "https://image.tmdb.org/t/p/w500/$poster_path"

fun MovieItem.getBackdropUrl() =
    "https://image.tmdb.org/t/p/w500/$backdrop_path"