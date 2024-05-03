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
    val backdropPath: String?
)