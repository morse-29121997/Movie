package com.morse.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.morse.core.viewmodel.BaseViewModel
import com.morse.core.viewmodel.Events
import com.morse.details.coordinator.MovieDetailsDirection
import com.morse.domain.base.State
import com.morse.domain.models.Movie
import com.morse.domain.models.MovieDetails
import com.morse.domain.usecases.AddMovieToFavouriteUseCase
import com.morse.domain.usecases.DeleteMovieFromFavouriteUseCase
import com.morse.domain.usecases.GetAllFavouriteMoviesUseCase
import com.morse.domain.usecases.GetMostPopularMovies2024UseCase
import com.morse.domain.usecases.GetMovieDetailUseCase
import com.morse.domain.usecases.GetNowPlayingMoviesUseCase
import com.morse.domain.usecases.GetSimilarMoviesUseCase
import com.morse.domain.usecases.IsExistInFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class MovieDetailState(
    val similarMovies: State<List<Movie>> = State.Loading,
    val details: State<MovieDetails> = State.Loading,
    val favouriteMovies: List<Movie> = arrayListOf(),
    val isFavourite: Boolean = false
)

sealed class MovieDetailEvents : Events {
    data object GetAllFavouriteMovies : MovieDetailEvents()
    data class AddMovieToFavourite(val movie: Movie) : MovieDetailEvents()
    data class DeleteMovieFromFavourite(val movie: Movie) : MovieDetailEvents()
    data class GetMovieDetail(val id: Int) : MovieDetailEvents()
    data class GetSimilarMovies(val id: Int) : MovieDetailEvents()
    data class IsFavourite(val movie: Movie) : MovieDetailEvents()
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val isExistInFavouriteMoviesUseCase: IsExistInFavouriteUseCase,
    private val allFavouriteMoviesUseCase: GetAllFavouriteMoviesUseCase,
    private val addToFavouriteUseCase: AddMovieToFavouriteUseCase,
    private val deleteMovieFromFavouriteUseCase: DeleteMovieFromFavouriteUseCase,
    private val detailMovieUseCase: GetMovieDetailUseCase,
    private val similarMoviesUseCase: GetSimilarMoviesUseCase,
) : BaseViewModel<MovieDetailState>() {

    init {
        state = MutableStateFlow(MovieDetailState())
    }

    override fun onEvent(event: Events) {
        when (event) {

            is MovieDetailEvents.IsFavourite -> {
                isFavouriteMovie(event.movie)
            }

            MovieDetailEvents.GetAllFavouriteMovies -> {
                getAllFavouriteMovies()
            }

            is MovieDetailEvents.DeleteMovieFromFavourite -> {
                update {
                    it.copy(isFavourite = false)
                }
                deleteMovieFromFavourite(event.movie)
            }

            is MovieDetailEvents.AddMovieToFavourite -> {
                update {
                    it.copy(isFavourite = true)
                }
                addMovieToFavourite(event.movie)
            }

            is MovieDetailEvents.GetSimilarMovies -> {
                getSimilarMovies(event.id)
            }

            is MovieDetailEvents.GetMovieDetail -> {
                getMovieDetails(event.id)
            }
        }
    }

    private fun addMovieToFavourite(movie: Movie) {
        viewModelScope.launch {
            addToFavouriteUseCase.invoke(movie)
        }
    }

    private fun deleteMovieFromFavourite(movie: Movie) {
        viewModelScope.launch {
            deleteMovieFromFavouriteUseCase.invoke(movie)
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

    private fun isFavouriteMovie(movie: Movie) {
        viewModelScope.launch {
            isExistInFavouriteMoviesUseCase.invoke(movie).onEach { state ->
                if (state is State.Success) {
                    update {
                        it.copy(isFavourite = state.data)
                    }
                }
            }.launchIn(this)
        }
    }

    private fun getMovieDetails(id: Int) {
        viewModelScope.launch {
            detailMovieUseCase.invoke(id).onEach { state ->
                update {
                    it.copy(details = state)
                }
            }.launchIn(this)
        }
    }

    private fun getSimilarMovies(id: Int) {
        viewModelScope.launch {
            similarMoviesUseCase.invoke(id).onEach { state ->
                update {
                    it.copy(similarMovies = state)
                }
            }.launchIn(this)
        }
    }

    override fun update(`do`: (MovieDetailState) -> MovieDetailState) = state.update(`do`)
}