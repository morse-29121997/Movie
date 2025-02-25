package com.morse.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.morse.data.database.MoviesDao
import com.morse.data.remote.MoviesApis
import com.morse.domain.models.Movie
import com.morse.domain.models.MovieDetails
import com.morse.domain.paging.IMoviesPagingSource
import com.morse.domain.repositories.IMoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.morse.data.mapper.MovieMapper.from as convertFrom
import com.morse.data.mapper.MovieMapper.to as convertTo

class MoviesRepository @Inject constructor(
    private val api: MoviesApis,
    private val db: MoviesDao,
    private val pagination: IMoviesPagingSource,
    private val scope: CoroutineScope
) : IMoviesRepository {

    override suspend fun isFavourite(movie: Int): Boolean {
        return db.isExist(movie)
    }

    override suspend fun saveToFavourite(movie: Movie) {
        scope.launch {
            db.insertMovie(convertTo(movie))
        }
    }

    override suspend fun getAllFavouriteMovies(): List<Movie> {
        return db.getFavouriteMovies().map { convertFrom(it) }
    }

    override suspend fun deleteFromFavourite(movie: Movie) {
        db.delete(movie.id)
    }

    override suspend fun getNowPlayingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { pagination }
        ).flow.cachedIn(scope)
    }

    override suspend fun getTopMovies2024(): List<Movie> {
        val response = api.getPopularMovies(1)
        if (response.isSuccessful) {
            return response.body()?.results ?: emptyList()
        }
        return emptyList()
    }

    override suspend fun getMovieDetails(id: Int): MovieDetails {
        val response = api.getMovieDetails(id)
        return if (response.isSuccessful) {
            response.body() ?: MovieDetails.Empty
        } else {
            MovieDetails.Empty
        }
    }

    override suspend fun getSimilarMovies(id: Int): List<Movie> {
        return api.getSimilarMovies(id , 1).body()?.results ?: emptyList()
    }
}