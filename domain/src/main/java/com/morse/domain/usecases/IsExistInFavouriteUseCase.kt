package com.morse.domain.usecases

import com.morse.domain.base.FlowUseCase
import com.morse.domain.base.State
import com.morse.domain.models.Movie
import com.morse.domain.repositories.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IsExistInFavouriteUseCase @Inject constructor(private val repo: IMoviesRepository) :
    FlowUseCase<Movie, Boolean>() {

    override suspend fun execute(args: Movie?): Flow<State<Boolean>> {
        return flowOf(repo.isFavourite(args?.id ?: 0)).map { State.Success(it) }
    }
}