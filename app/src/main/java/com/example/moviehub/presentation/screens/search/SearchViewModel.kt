package com.example.moviehub.presentation.screens.search

import androidx.compose.material3.SearchBar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehub.common.Resource
import com.example.moviehub.domain.model.Movie
import com.example.moviehub.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {
    private val _state = MutableStateFlow(SearchState())

    val state: StateFlow<SearchState> =_state.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500L)
                .filter { it.length>=2 }
                .distinctUntilChanged()
                .collectLatest { query ->
                    perfomSearch(query)
                }
        }
    }

    private fun perfomSearch(query: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when(val result = repository.searchMovies(query)){
                is Resource.Success ->{
                    _state.value = _state.value.copy(
                        movie = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error ->{
                    _state.value = _state.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
                is Resource.Loading->{
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }

            }
        }
    }

    fun onQueryChange(newQuery:String){
        _state.value = _state.value.copy(query = newQuery)
        _searchQuery.value = newQuery
    }
}

data class SearchState(
    val isLoading:Boolean = false,
    val error:String?= null,
    val movie:List<Movie> = emptyList(),
    val query:String = ""
)