package com.david.movie.movielab.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {
    // Insert a new movie or replace the existing one based on the primary key conflict
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceMovie(movie: MovieDBItem): Long

    // Update an existing movie
    @Update
    suspend fun updateMovie(movie: MovieDBItem)

    // Delete a movie by ID
    @Query("DELETE FROM movie WHERE id = :id")
    suspend fun deleteMovieById(id: Int)

    // Get a movie by ID
    @Query("SELECT * FROM movie WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieDBItem?

    // Fetch all movies from the database
    @Query("SELECT * FROM movie")
    fun getAllMoviesAsFlow(): Flow<List<MovieDBItem>>

    // Check if a specific movie exists
    @Query("SELECT EXISTS(SELECT * FROM movie WHERE id = :id)")
    suspend fun isMovieExists(id: Int): Boolean

    // Delete all movies from the database
    @Query("DELETE FROM movie")
    suspend fun deleteAllMovies()

    // Get movies with a specific title (useful for search functionality)
    @Query("SELECT * FROM movie WHERE title LIKE :title")
    suspend fun findMoviesByTitle(title: String): List<MovieDBItem>
}
