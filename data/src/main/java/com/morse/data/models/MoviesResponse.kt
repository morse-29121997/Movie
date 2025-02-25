package com.morse.data.models

import com.google.gson.annotations.SerializedName
import com.morse.domain.models.Movie

data class MoviesResponse(
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    val results: ArrayList<Movie>
)


