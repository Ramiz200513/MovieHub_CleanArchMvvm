package com.example.moviehub.presentation.screens.auth

import android.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviehub.common.Resource
import com.example.moviehub.domain.repository.AuthRepository
import com.example.moviehub.domain.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()


    fun onEmailChange(newValue: String) {
        _email.value = newValue
    }

    fun onPasswordChange(newValue: String) {
        _password.value = newValue
    }
    fun onGoogleSignIn(idToken: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = repository.signInWithGoogle(idToken)
            handleAuthResult(result)
        }
    }
    fun signUp() {
        val currentEmail = _email.value
        val currentPass = _password.value

        if (currentEmail.isBlank() || currentPass.isBlank()) {
            _state.value = _state.value.copy(error = "Fill all fields")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = repository.signUp(currentEmail, currentPass)

            handleAuthResult(result)
        }
    }

    fun signIn() {
        val currentEmail = _email.value
        val currentPass = _password.value

        if (currentEmail.isBlank() || currentPass.isBlank()) {
            _state.value = _state.value.copy(error = "Fill all fields")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = repository.signIn(currentEmail, currentPass)
            handleAuthResult(result)
        }
    }
    private fun handleAuthResult(result: Resource<User>) {
        when (result) {
            is Resource.Success -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    user = result.data,
                    error = null
                )
            }
            is Resource.Error -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = result.message ?: "Unknown error"
                )
            }
            is Resource.Loading -> Unit
        }
    }
}

data class AuthState(
    val user: User? =  null,
    val isLoading:Boolean = false,
    val error:String? = null
)