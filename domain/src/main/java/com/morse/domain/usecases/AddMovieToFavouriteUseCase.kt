package com.morse.domain.usecases

import com.morse.domain.base.UseCase
import com.morse.domain.models.Movie
import com.morse.domain.repositories.IMoviesRepository
import javax.inject.Inject

class AddMovieToFavouriteUseCase @Inject constructor(private val repo: IMoviesRepository) :
    UseCase<Movie, Unit>() {
    override suspend fun execute(args: Movie?) {
        if (args != null) {
            repo.saveToFavourite(args)
        }
    }
}