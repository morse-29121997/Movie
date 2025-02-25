package com.morse.feature_view_movies.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.morse.domain.models.Movie
import com.morse.feature_view_movies.databinding.MovieItemLayoutBinding

class MoviesAdapter(
    private val onMovieClickListener: (Movie) -> Unit,
    private val onFavouriteClickListener: (Movie, Boolean) -> Unit,
) : PagingDataAdapter<Movie, MoviesAdapter.MovieViewHolder>(
    PAGEDATA_COMPARATOR.getDiffUtil()
) {

    private val favouriteMovies = arrayListOf<Movie>()

    fun addFavouriteMovies(movies: List<Movie>) {
        favouriteMovies.clear()
        favouriteMovies.addAll(movies)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            MovieItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.binding.root.setOnClickListener { onMovieClickListener.invoke(item) }
        holder.binding.movie = item.apply {
            isFavourite = favouriteMovies.count { it.title == title } > 0
        }
        holder.binding.favIv.setOnClickListener {
            item.isFavourite = !item.isFavourite
            holder.binding.movie = item
            if (item.isFavourite) {
                onFavouriteClickListener.invoke(item, true)
            } else {
                onFavouriteClickListener.invoke(item, false)
            }
        }
    }

    class MovieViewHolder(val binding: MovieItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    object PAGEDATA_COMPARATOR {
        private val `object` =
            object : DiffUtil.ItemCallback<Movie>() {
                override fun areItemsTheSame(
                    oldItem: Movie,
                    newItem: Movie
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Movie,
                    newItem: Movie
                ): Boolean {
                    return oldItem.id == newItem.id
                }

            }

        fun getDiffUtil() = `object`
    }
}