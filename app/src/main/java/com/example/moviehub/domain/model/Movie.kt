package com.example.moviehub.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val rating: Double,
    val isFavorite: Boolean = false
)