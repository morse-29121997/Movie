package com.morse.domain.models

data class MovieDetails(
    val id: Int,
    val title: String="",
    val overview: String,
    val release_date: String,
    val genre_ids: List<Int>,
    val genres: List<Genre>,
    val runtime: Int,
    val vote_average: Double?,
    val vote_count: Int,
    val popularity: Double,
    val backdrop_path: String?,
    val poster_path: String?,
    val original_language: String?,
    val homepage: String?,
    val status: String,
    val tagline: String? ,
    val video : Boolean?= false,
    val isFavourite : Boolean?= false
) {
    fun getGenre(): String {
        return genres.firstOrNull()?.name ?: "-"
    }

    fun getFullBackdropUrl(): String {
        return if (backdrop_path != null) {
            "https://image.tmdb.org/t/p/w500$backdrop_path"
        } else {
            ""
        }
    }

    fun getFullPosterUrl(): String {
        return if (poster_path != null) {
            "https://image.tmdb.org/t/p/w500$poster_path"
        } else {
            ""
        }
    }

    companion object {
        val Empty = MovieDetails(
            0,
            "",
            "",
            "",
            emptyList(),
            emptyList(),
            0,
            0.0,
            0,
            0.0,
            "",
            "",
            "",
            "",
            "",
            ""
        )
    }
}


data class Genre(
    val id: Int,
    val name: String
)