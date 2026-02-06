package com.example.moviehub.domain.repository

import com.example.moviehub.common.Resource
import com.example.moviehub.data.local.MovieDao
import com.example.moviehub.data.mapper.toDomain
import com.example.moviehub.data.mapper.toEntity
import com.example.moviehub.data.mapper.toFirestoreMap
import com.example.moviehub.data.mapper.toMovie
import com.example.moviehub.data.remote.api.TmdbApi
import com.example.moviehub.domain.model.Movie
import com.example.moviehub.domain.model.MovieDetails
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi,
    private val dao: MovieDao,
    private val auth: AuthRepository,
    private val firestore: FirebaseFirestore
) : MovieRepository {
    override suspend fun getMovieTrends(page: Int): Resource<List<Movie>> {
        return try {
            val result = api.getTrendingMovies(page = page)
            val domainMovies = result.results.map { it.toDomain() }
            Resource.Success(domainMovies)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun searchMovies(query: String): Resource<List<Movie>> {
        return try {
            val result = api.searchMovies(query)
            val domainMovie = result.results.map { it.toDomain() }
            Resource.Success(domainMovie)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Resource<MovieDetails> {
        return try {
            val dto = api.getMovieDetails(movieId)
            val domainMovie = dto.toDomain()
            Resource.Success(domainMovie)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error")
        }
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return dao.getFavoriteMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun isMovieFavorite(movieId: Int): Boolean {
        return dao.isMovieFavorite(movieId)

    }

    override suspend fun insertFavorite(movie: Movie) {
        dao.insertFavorite(movie.toEntity())
        val currentUser = auth.getCurrentUser()
        if(currentUser !=null){
            try {
                firestore.collection("users")
                    .document(currentUser.id)
                    .collection("favorites")
                    .document(movie.id.toString())
                    .set(movie.toFirestoreMap())
                    .await()
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun deleteFavorite(movie: Movie) {
        dao.deleteFavorite(movie.toEntity())
        val currentUser = auth.getCurrentUser()
        if(currentUser !=null){
            try {
                firestore.collection("users")
                    .document(currentUser.id)
                    .collection("favorites")
                    .document(movie.id.toString())
                    .delete()
                    .await()
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun syncFavoritesFromCloud() {
        val currentUser = auth.getCurrentUser()
        if(currentUser != null){
            try {
                val snapshot = firestore.collection("users")
                    .document(currentUser.id)
                    .collection("favorites")
                    .get()
                    .await()
                val movies = snapshot.documents.mapNotNull{ doc->
                    doc.data?.toMovie()
                }
                val enteties = movies.map { movie ->
                    movie.toEntity()
                }
                dao.insertMovies(enteties)
            } catch (e: Exception) {
            e.printStackTrace()
        }
        }
    }

}