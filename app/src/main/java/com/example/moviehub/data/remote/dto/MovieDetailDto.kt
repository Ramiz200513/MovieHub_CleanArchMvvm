package com.example.moviehub.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
    val id:Int,
    val title:String,
    val overview:String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("release_date") val releaseDate: String = "",
    val genres: List<GenreDto> = emptyList(),
    val videos: VideosContainerDto? = null
)
