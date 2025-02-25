package com.morse.domain.usecases

import com.morse.domain.base.FlowUseCase
import com.morse.domain.base.State
import com.morse.domain.base.UseCase
import com.morse.domain.models.Movie
import com.morse.domain.repositories.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllFavouriteMoviesUseCase @Inject constructor(private val repo: IMoviesRepository) :
    FlowUseCase<Unit, Movie>() {

    override suspend fun execute(args: Unit?): Flow<State<Movie>> {
      return  flowOf(repo.getAllFavouriteMovies()).map { State.Success(it) as State<Movie> }
    }

}