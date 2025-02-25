package com.morse.data.paginations

import com.morse.data.remote.MoviesApis
import com.morse.domain.models.Movie
import com.morse.domain.paging.IMoviesPagingSource
import java.io.IOException
import javax.inject.Inject


class NowPlayingMoviesPagingSource @Inject constructor(
    private val moviesApis: MoviesApis,
) : IMoviesPagingSource() {

    private var totalCurrentMovies : Int = 0
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = moviesApis.getNowPlayingMovies(page)
            if (response.isSuccessful) {
                val movies = response.body()?.results ?: emptyList()
                totalCurrentMovies +=movies.size
                val nextKey = if(response.body()?.totalResults == 0) null else if (totalCurrentMovies < (response.body()?.totalResults ?: 0)) null else page + 1
                val prevKey = if (page == 1) null else page - 1
                LoadResult.Page(movies, prevKey, nextKey)
            } else {
                LoadResult.Error(retrofit2.HttpException(response))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: retrofit2.HttpException) {
            LoadResult.Error(e)
        }
    }
}