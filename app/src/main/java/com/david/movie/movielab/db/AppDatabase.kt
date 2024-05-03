package com.david.movie.movielab.db

import androidx.room.Database
import androidx.room.RoomDatabase




@Database(entities = [MovieDBItem::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}