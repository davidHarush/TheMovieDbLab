package com.david.movie.movielab.db

import androidx.room.Entity
import androidx.room.Index


@Entity(tableName = "movie", primaryKeys = ["id"], indices = [Index(value = ["id"], unique = true)])
data class MovieDBItem(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val posterPath: String?,
    val backdropPath: String?,
    // lastWatch can be null if the movie is add to favorite from main screen,
    // last watch is only form movie detail screen
    val lastWatch: String? = null,
    val isFavorite: Boolean = false
)