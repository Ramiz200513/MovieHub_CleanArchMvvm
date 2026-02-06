package com.example.moviehub.domain.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val description: String,
    val posterPath: String?,
    val backdropPath: String?,
    val rating: Double,
    val genres: List<String>,
    val videoKey: String?
)