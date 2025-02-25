package com.morse.feature_view_movies.ui

import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.paging.LoadState
import com.morse.core.activity.BaseDataBindingActivity
import com.morse.core.activity.collect
import com.morse.core.adapter.configureAdapter
import com.morse.core.adapter.layoutRes
import com.morse.core.adapter.onBind
import com.morse.core.adapter.onItemClick
import com.morse.core.adapter.withData
import com.morse.core.adapter.withRecyclerView
import com.morse.core.binding_adapters.viewIf
import com.morse.details.coordinator.MovieDetailsDirection
import com.morse.domain.base.State
import com.morse.domain.models.Movie
import com.morse.feature_view_movies.R
import com.morse.feature_view_movies.databinding.ActivityMoviesListBinding
import com.morse.feature_view_movies.databinding.PopularMovieItemLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesListActivity :
    BaseDataBindingActivity<ActivityMoviesListBinding>(R.layout.activity_movies_list) {
    private val vm: MoviesViewModel by viewModels()
    private val pagingAdapter: MoviesAdapter = MoviesAdapter({ movie ->
        MovieDetailsDirection.navigate(
            this,
            bundleOf(MovieDetailsDirection.movieKey to movie)
        )
    }) { movie, isFavourite ->
        if (isFavourite) {
            vm.onEvent(MoviesEvents.AddMovieToFavourite(movie = movie))
        } else {
            vm.onEvent(MoviesEvents.DeleteMovieFromFavourite(movie = movie))
        }
    }

    override fun onResume() {
        super.onResume()
        vm.run {
            onEvent(MoviesEvents.GetAllFavouriteMovies)
            onEvent(MoviesEvents.GetMostPopularMovies2024)
            onEvent(MoviesEvents.GetNowPlayingMovies)
        }
    }

    override fun configureActions() {
        binding.nowPlayingMoviesRv.adapter = pagingAdapter
        observeOnViewModel()
    }

    private fun setup2024PopularAdapter(movies: ArrayList<Movie>) {
        configureAdapter<Movie, PopularMovieItemLayoutBinding> {
            withRecyclerView(binding.topMoviesRv)
            layoutRes(R.layout.popular_movie_item_layout)
            onBind { dataObject: Movie, dataBinding: PopularMovieItemLayoutBinding ->
                dataBinding.movie = dataObject
                dataBinding.favIv.setOnClickListener {
                    dataObject.isFavourite = !dataObject.isFavourite
                    dataBinding.movie = dataObject
                    if (dataObject.isFavourite) {
                        vm.onEvent(MoviesEvents.AddMovieToFavourite(movie = dataObject))
                    } else {
                        vm.onEvent(MoviesEvents.DeleteMovieFromFavourite(movie = dataObject))
                    }
                }
                dataBinding.executePendingBindings()
            }
            onItemClick { movie ->
                MovieDetailsDirection.navigate(
                    this@MoviesListActivity,
                    bundleOf(MovieDetailsDirection.movieKey to movie)
                )
            }

            withData(
                movies.onEach { movie ->
                    movie.isFavourite =
                        vm.state.value.favouriteMovies.count { it.title == movie.title } > 0
                }
            )
        }
    }

    private fun observeOnViewModel() {
        collect(vm.state) { state ->
            viewIf(binding.loading, state.popular2024Movies is State.Loading)
            viewIf(binding.popularMovies2024Success, state.popular2024Movies is State.Success)
            viewIf(binding.nowPlayingMoviesSuccess, state.popular2024Movies is State.Success)
            if (state.popular2024Movies is State.Success) {
                setup2024PopularAdapter(state.popular2024Movies.data as ArrayList<Movie>)
            }
            pagingAdapter.submitData(lifecycle, state.nowPlayingMovies)
        }
        collect(pagingAdapter.loadStateFlow) { loadState ->
            when (loadState.refresh) {
                is LoadState.NotLoading -> {
                    if (loadState.append.endOfPaginationReached && pagingAdapter.itemCount == 0) {
                        // Show Empty
                    } else {
                        // Show Success
                    }
                }

                is LoadState.Loading -> {


                }

                is LoadState.Error -> {

                }

            }
        }
    }

}