package com.example.moviehub.domain.repository

import com.example.moviehub.common.Resource
import com.example.moviehub.domain.user.User
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Resource<User>
    suspend fun signIn(email: String, password: String): Resource<User>
    fun signOut()
    suspend fun signInWithGoogle(idToken: String): Resource<User>
    fun getCurrentUser(): User?

}