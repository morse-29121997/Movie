package com.morse.feature_view_movies.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.morse.core.viewmodel.BaseViewModel
import com.morse.core.viewmodel.Events
import com.morse.domain.base.State
import com.morse.domain.models.Movie
import com.morse.domain.usecases.AddMovieToFavouriteUseCase
import com.morse.domain.usecases.DeleteMovieFromFavouriteUseCase
import com.morse.domain.usecases.GetAllFavouriteMoviesUseCase
import com.morse.domain.usecases.GetMostPopularMovies2024UseCase
import com.morse.domain.usecases.GetNowPlayingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MoviesState(
    val popular2024Movies: State<List<Movie>> = State.Loading,
    val nowPlayingMovies: PagingData<Movie> = PagingData.empty(),
    val favouriteMovies: List<Movie> = arrayListOf()
)

sealed class MoviesEvents : Events {
    data object GetAllFavouriteMovies : MoviesEvents()
    data class AddMovieToFavourite(val movie: Movie) : MoviesEvents()
    data class DeleteMovieFromFavourite(val movie: Movie) : MoviesEvents()
    data object GetMostPopularMovies2024 : MoviesEvents()
    data object GetNowPlayingMovies : MoviesEvents()
}

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val allFavouriteMoviesUseCase: GetAllFavouriteMoviesUseCase,
    private val addToFavouriteUseCase: AddMovieToFavouriteUseCase,
    private val deleteMovieFromFavouriteUseCase: DeleteMovieFromFavouriteUseCase,
    private val popularMoviesUseCase: GetMostPopularMovies2024UseCase,
    private val nowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
) : BaseViewModel<MoviesState>() {

    init {
        state = MutableStateFlow(MoviesState())
        onEvent(MoviesEvents.GetMostPopularMovies2024)
        onEvent(MoviesEvents.GetNowPlayingMovies)
        onEvent(MoviesEvents.GetAllFavouriteMovies)
    }

    override fun onEvent(event: Events) {
        when (event) {

            MoviesEvents.GetAllFavouriteMovies -> {
                getAllFavouriteMovies()
            }

            is MoviesEvents.DeleteMovieFromFavourite -> {
                deleteMovieFromFavourite(event.movie)
                getAllFavouriteMovies()
            }

            is MoviesEvents.AddMovieToFavourite -> {
                addMovieToFavourite(event.movie)
                getAllFavouriteMovies()
            }

            is MoviesEvents.GetMostPopularMovies2024 -> {
                getPopularMoviesAt2024()
            }

            is MoviesEvents.GetNowPlayingMovies -> {
                getNowPlayingMovies()
            }
        }
    }

    private fun addMovieToFavourite(movie: Movie) {
        viewModelScope.launch {
            addToFavouriteUseCase.invoke(movie)
            getAllFavouriteMovies()
        }
    }

    private fun deleteMovieFromFavourite(movie: Movie) {
        viewModelScope.launch {
            deleteMovieFromFavouriteUseCase.invoke(movie)
            getAllFavouriteMovies()
        }
    }

    private fun getAllFavouriteMovies() {
        viewModelScope.launch {
            allFavouriteMoviesUseCase.invoke().onEach { state ->
                if (state is State.Success) {
                    update {
                        it.copy(favouriteMovies = (state as State.Success<List<Movie>>).data)
                    }
                }
            }.launchIn(this)
        }
    }

    private fun getPopularMoviesAt2024() {
        viewModelScope.launch {
            popularMoviesUseCase.invoke().onEach { state ->
                update {
                    it.copy(popular2024Movies = state)
                }
            }.launchIn(this)
        }
    }

    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            nowPlayingMoviesUseCase.invoke().onEach { state ->
                update {
                    it.copy(nowPlayingMovies = state)
                }
            }.launchIn(this)
        }
    }

    override fun update(`do`: (MoviesState) -> MoviesState) = state.update(`do`)
}