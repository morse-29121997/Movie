package com.morse.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.morse.domain.models.Movie

abstract class IMoviesPagingSource : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}