package com.morse.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.morse.data.database.MovieEntity
import com.morse.data.database.MoviesDao


@Database(entities = [MovieEntity::class], version = 1)
abstract class MoviesDataBase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}