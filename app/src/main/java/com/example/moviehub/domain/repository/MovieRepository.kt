package com.example.moviehub.domain.repository

import com.example.moviehub.common.Resource
import com.example.moviehub.domain.model.Movie
import com.example.moviehub.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getMovieTrends(page: Int): Resource<List<Movie>>
    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetails>
    fun getFavoriteMovies(): Flow<List<Movie>>
    suspend fun isMovieFavorite(movieId: Int): Boolean
    suspend fun insertFavorite(movie: Movie)
    suspend fun searchMovies(query: String): Resource<List<Movie>>
    suspend fun deleteFavorite(movie: Movie)
    suspend fun syncFavoritesFromCloud()
}