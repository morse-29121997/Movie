package com.morse.domain.models

import java.io.Serializable

data class Movie(
    val id: Int,
    val poster_path: String,
    val release_date: String,
    val backdrop_path: String?,
    val title: String,
    val overview: String,
    val video: Boolean,
    val vote_average: Double ,
    var isFavourite : Boolean = false
) : Serializable{
    fun getVote() = "${vote_average} of 10"
    fun getFullPosterUrl(): String {
        return if (poster_path != null) {
            "https://image.tmdb.org/t/p/w500$poster_path"
        } else {
            ""
        }
    }

    fun getFullBackdropUrl(): String {
        return if (backdrop_path != null) {
            "https://image.tmdb.org/t/p/w500$backdrop_path"
        } else {
            ""
        }
    }
}