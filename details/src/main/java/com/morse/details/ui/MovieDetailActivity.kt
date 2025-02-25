package com.morse.details.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
import com.morse.details.R
import com.morse.details.coordinator.MovieDetailsDirection
import com.morse.details.databinding.ActivityMovieDetailBinding
import com.morse.details.databinding.SimilarMovieItemLayoutBinding
import com.morse.domain.base.State
import com.morse.domain.models.Movie
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity :
    BaseDataBindingActivity<ActivityMovieDetailBinding>(R.layout.activity_movie_detail) {
    private val vm: DetailViewModel by viewModels()
    private val movie by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(
                MovieDetailsDirection.movieKey,
                Movie::class.java
            )
        } else {
            intent.getSerializableExtra(
                MovieDetailsDirection.movieKey,
            ) as Movie
        }
    }

    override fun onResume() {
        super.onResume()
        vm.run {
            onEvent(MovieDetailEvents.GetMovieDetail(movie?.id ?: 0))
            onEvent(MovieDetailEvents.GetSimilarMovies(movie?.id ?: 0))
            onEvent(MovieDetailEvents.GetAllFavouriteMovies(movie!!))
        }
    }

    override fun configureActions() {
        observeOnViewModel()
        binding.movieCloseIv.setOnClickListener {
            finish()
        }
        binding.favIv.setOnClickListener{
            if (vm.state.value.isFavourite) {
                if(movie != null)
                     vm.onEvent(MovieDetailEvents.DeleteMovieFromFavourite(movie = movie!!))
            } else {
                if(movie != null)
                    vm.onEvent(MovieDetailEvents.AddMovieToFavourite(movie = movie!!))
            }
            binding.isFavourite = vm.state.value.isFavourite
        }
    }

    private fun setupSimilarMoviesAdapter(movies: ArrayList<Movie>) {
        configureAdapter<Movie, SimilarMovieItemLayoutBinding> {
            withRecyclerView(binding.similarRv)
            layoutRes(R.layout.similar_movie_item_layout)
            onBind { dataObject: Movie, dataBinding: SimilarMovieItemLayoutBinding ->
                dataBinding.movie = dataObject
                dataBinding.favIv.setOnClickListener {
                    dataObject.isFavourite = !dataObject.isFavourite
                    dataBinding.movie = dataObject
                    if (dataObject.isFavourite) {
                        vm.onEvent(MovieDetailEvents.AddMovieToFavourite(movie = dataObject))
                    } else {
                        vm.onEvent(MovieDetailEvents.DeleteMovieFromFavourite(movie = dataObject))
                    }
                }
                dataBinding.executePendingBindings()
            }
            onItemClick { movie ->
                MovieDetailsDirection.navigate(
                    this@MovieDetailActivity,
                    bundleOf(MovieDetailsDirection.movieKey to movie)
                )
            }

            withData(
                movies.onEach { movie ->  movie.isFavourite = vm.state.value.favouriteMovies.count { it.title == movie.title } > 0}
            )
        }
    }

    private fun observeOnViewModel() {
        collect(vm.state) { state ->
            viewIf(binding.loading, state.similarMovies is State.Loading)
            viewIf(binding.success, state.details is State.Success)
            if (state.similarMovies is State.Success) {
                setupSimilarMoviesAdapter(state.similarMovies.data as ArrayList<Movie>)
            }
            if (state.details is State.Success) {
                binding.detail = state.details.data
                binding.isFavourite = state.isFavourite
            }

        }
    }
}