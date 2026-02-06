package com.example.moviehub.presentation.screens.favorite

import androidx.compose.foundation.rememberPlatformOverscrollFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviehub.data.mapper.toEntity
import com.example.moviehub.domain.model.Movie
import com.example.moviehub.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
   private val repository: MovieRepository
): ViewModel() {
    private val _isSorted = MutableStateFlow(false)
    val isSorted: StateFlow<Boolean> = _isSorted.asStateFlow()
    val favorites: StateFlow<List<Movie>> = combine(
        repository.getFavoriteMovies(),
        _isSorted
    ){ movies,isSorted ->
        if(isSorted){
            movies.sortedByDescending { it.rating }
        }else{
            movies
        }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()

    )
    fun toggleSort(){
        _isSorted.value = !_isSorted.value
    }
    fun deleteFavorite(movie: Movie){
        viewModelScope.launch {
            repository.deleteFavorite(movie = movie)
        }
    }
}