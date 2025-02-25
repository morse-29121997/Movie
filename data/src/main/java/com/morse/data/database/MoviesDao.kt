package com.morse.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("SELECT EXISTS(SELECT * FROM favourite_movies WHERE id = :id)")
    suspend fun isExist (id : Int) : Boolean

    @Query("DELETE FROM favourite_movies WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM favourite_movies")
    suspend fun getFavouriteMovies(): List<MovieEntity>
}