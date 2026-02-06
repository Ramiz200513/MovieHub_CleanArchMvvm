package com.example.moviehub.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviehub.common.Resource
import com.example.moviehub.domain.model.Movie
import com.example.moviehub.domain.repository.MovieRepository
import com.example.moviehub.domain.repository.MovieRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel(){
    private val _trendingMovies = MutableStateFlow<List<Movie>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage =MutableStateFlow<String?>(null)

    val state: StateFlow<HomeState> = combine(
        _trendingMovies,
        _isLoading,
        _errorMessage,
    ){  movies ,isLoading,error ->
        HomeState(
            movies,
            isLoading,
            error
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeState(isLoading = true)
    )
    init {
        loadTrendingMovies()
        syncData()
    }
    fun syncData(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.syncFavoritesFromCloud()
        }
    }
    fun loadTrendingMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.getMovieTrends(page = 1)
            when (result) {
                is Resource.Success -> {
                    _trendingMovies.value = result.data ?: emptyList()
                    _isLoading.value = false
                }
                is Resource.Error -> {
                    _errorMessage.value = result.message ?: "Unknown error"
                    _isLoading.value = false
                }
                is Resource.Loading -> {
                    _isLoading.value = true
                }
            }
        }
    }
}
