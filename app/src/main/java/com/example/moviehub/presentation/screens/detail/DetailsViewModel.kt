package com.example.moviehub.presentation.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehub.common.Resource
import com.example.moviehub.data.mapper.toMovie
import com.example.moviehub.domain.model.MovieDetails
import com.example.moviehub.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.BooleanArraySerializer
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(DetailsState())
    val state: StateFlow<DetailsState> = _state
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()
    init {
        savedStateHandle.get<Int>("movieId")?.let { id ->
            loadMovieDetails(id)
            checkIfFavorite(id)
        }
    }
    private fun loadMovieDetails(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = repository.getMovieDetails(id)) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(movie = result.data, isLoading = false)
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(error = result.message, isLoading = false)
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }
    private fun checkIfFavorite(id:Int){
        viewModelScope.launch {
            val exist = repository.isMovieFavorite(id)
            _isFavorite.value = exist
        }
    }
    fun toggleFavorite(movieDetails: MovieDetails) {
        viewModelScope.launch {
            val movie = movieDetails.toMovie()

            if (_isFavorite.value) {
                repository.deleteFavorite(movie)
                _isFavorite.value = false
            } else {
                repository.insertFavorite(movie)
                _isFavorite.value = true
            }
        }
    }
}