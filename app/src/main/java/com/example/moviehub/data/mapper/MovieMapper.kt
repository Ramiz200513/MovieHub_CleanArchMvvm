package com.example.moviehub.data.mapper

import androidx.compose.foundation.text.input.rememberTextFieldState
import com.example.moviehub.data.local.MovieDao
import com.example.moviehub.data.local.MovieEntity
import com.example.moviehub.data.remote.dto.MovieDetailsDto
import com.example.moviehub.data.remote.dto.MovieDto
import com.example.moviehub.domain.model.Movie
import com.example.moviehub.domain.model.MovieDetails

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
        backdropPath = backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" },
        rating = voteAverage,
        isFavorite = false
    )
}

fun MovieDetailsDto.toDomain(): MovieDetails {
    val trailer = videos?.results?.find { it.site == "YouTube" && it.type == "Trailer" }

    return MovieDetails(
        id = id,
        title = title,
        description = overview,
        posterPath = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
        backdropPath = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" },
        rating = voteAverage,
        genres = genres.map { it.name },
        videoKey = trailer?.key
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        posterPath = posterPath,
        rating = rating
    )
}

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        backdropPath = null,
        rating = rating,
        isFavorite = true
    )
}

fun MovieDetails.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        backdropPath = backdropPath,
        rating = rating,
        isFavorite = true
    )
}
fun Movie.toFirestoreMap(): Map<String, Any?> {
    return hashMapOf(
        "id" to id,
        "title" to title,
        "posterPath" to posterPath,
        "backdropPath" to backdropPath,
        "rating" to rating,
        "isFavorite" to isFavorite,
        "timestamp" to System.currentTimeMillis()
    )
}

fun Map<String, Any?>.toMovie(): Movie {
    return Movie(
        id = (this["id"] as? Long)?.toInt() ?: 0,
        title = this["title"] as? String ?: "",
        posterPath = this["posterPath"] as? String ?: "",
        backdropPath = this["backdropPath"] as? String ?: "",
        rating = (this["rating"] as? Number)?.toDouble() ?: 0.0,
        isFavorite = true
    )
}