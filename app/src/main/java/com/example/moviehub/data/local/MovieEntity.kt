package com.example.moviehub.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favorite_movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String?,
    val rating: Double,
    val addedAt: Long = System.currentTimeMillis()
)