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

class GetMostPopularMovies2024UseCase @Inject constructor(private val repo: IMoviesRepository) :
    FlowUseCase<Unit, List<Movie>>() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun execute(args: Unit?): Flow<State<List<Movie>>> {
        return flowOf(repo.getTopMovies2024()).mapLatest { it.filter { it.release_date.startsWith("2024") } }
            .map {
                State.Success(it)
            }
    }
}