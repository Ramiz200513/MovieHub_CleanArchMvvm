package com.example.moviehub.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponseDto(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)