package com.example.moviehub.domain.repository

import com.example.moviehub.common.Resource
import com.example.moviehub.domain.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {
    override suspend fun signUp(
        email: String,
        password: String
    ): Resource<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user?.toDomain()
            if (firebaseUser != null) {
                Resource.Success(firebaseUser)
            } else {
                Resource.Error("User creation failed: Unknown error")
            }
        }catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Resource<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()
            val user = result.user?.toDomain()
            if (user != null){
                Resource.Success(user)
            }else{
                Resource.Error("User creation failed: Unknown error")
            }
        }catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun signInWithGoogle(idToken: String): Resource<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken,null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val user = result.user?.toDomain()
            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("Google sign in failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Google Sign in error")
        }
        }


    override fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toDomain()
    }
    private fun FirebaseUser.toDomain(): User{
        return User(
            id = this.uid,
            email = this.email ?: "",
            username = this.displayName
        )
    }

}