package com.example.moviehub.presentation.screens.home

import com.example.moviehub.domain.model.Movie

data class HomeState (
    val trendingMovies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)