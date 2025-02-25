package com.morse.domain.usecases

import com.morse.domain.base.FlowUseCase
import com.morse.domain.base.State
import com.morse.domain.models.Movie
import com.morse.domain.repositories.IMoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetSimilarMoviesUseCase @Inject constructor(private val repo: IMoviesRepository) :
    FlowUseCase<Int, List<Movie>>() {
    override suspend fun execute(args: Int?): Flow<State<List<Movie>>> {
        return flowOf(repo.getSimilarMovies(args ?: 0))
            .map { State.Success(it) }
    }
}