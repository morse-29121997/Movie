package com.morse.domain.repositories

import androidx.paging.PagingData
import com.morse.domain.models.Movie
import com.morse.domain.models.MovieDetails
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {

    suspend fun isFavourite(movie: Int): Boolean

    suspend fun saveToFavourite(movie: Movie)

    suspend fun getAllFavouriteMovies(): List<Movie>

    suspend fun deleteFromFavourite(movie: Movie)

    suspend fun getNowPlayingMovies(): Flow<PagingData<Movie>>

    suspend fun getTopMovies2024(): List<Movie>

    suspend fun getMovieDetails(id: Int) : MovieDetails

    suspend fun getSimilarMovies(id: Int) : List<Movie>

}