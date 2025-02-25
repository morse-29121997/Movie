package com.morse.domain.usecases

import com.morse.domain.base.FlowUseCase
import com.morse.domain.base.State
import com.morse.domain.models.Movie
import com.morse.domain.models.MovieDetails
import com.morse.domain.repositories.IMoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(private val repo: IMoviesRepository) :
    FlowUseCase<Int, MovieDetails>() {
    override suspend fun execute(args: Int?): Flow<State<MovieDetails>> {
        return flowOf(repo.getMovieDetails(args ?: 0))
            .map { State.Success(it) }
    }
}