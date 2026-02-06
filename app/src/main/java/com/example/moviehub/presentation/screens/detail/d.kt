package com.example.moviehub.presentation.screens.detail

import com.example.moviehub.domain.model.MovieDetails

data class DetailsState(
    val movie: MovieDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
