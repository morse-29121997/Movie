package com.morse.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favourite_movies")
data class MovieEntity (
    @PrimaryKey(autoGenerate = false)
    val id: Int=0,
    @ColumnInfo val posterPath: String,
    @ColumnInfo val backdropPath: String,
    val releaseDate: String,
    val overview: String,
    @ColumnInfo val title: String,
    val video: Boolean,
    val rating: Double
)