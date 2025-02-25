package com.morse.domain.usecases

import androidx.paging.PagingData
import com.morse.domain.base.UseCase
import com.morse.domain.models.Movie
import com.morse.domain.repositories.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNowPlayingMoviesUseCase @Inject constructor(private val repo: IMoviesRepository ) :
    UseCase<Unit, Flow<PagingData<Movie>>>() {

    override suspend fun execute(args: Unit?): Flow<PagingData<Movie>> {
        return repo.getNowPlayingMovies()
    }
}