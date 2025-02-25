package com.morse.data.mapper

import com.morse.data.database.MovieEntity
import com.morse.domain.models.Movie

object MovieMapper : Mapper<MovieEntity, Movie> {
    override fun from(input: MovieEntity): Movie {
        return Movie(
            id = input.id,
            title = input.title,
            poster_path = input.posterPath,
            release_date = input.releaseDate,
            overview = input.overview,
            video = input.video,
            vote_average = input.rating,
            backdrop_path = input.backdropPath ,
            isFavourite = true
        )
    }

    override fun to(outputType: Movie): MovieEntity {
        return MovieEntity(
            posterPath = outputType.poster_path,
            backdropPath = outputType.backdrop_path ?: "",
            releaseDate = outputType.release_date,
            overview = outputType.overview,
            title = outputType.title,
            video = outputType.video,
            rating = outputType.vote_average,
        )
    }
}