package com.example.moviehub.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideosContainerDto(
    val results: List<VideoDto>
)

@Serializable
data class VideoDto(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String,
    @SerialName("official") val isOfficial: Boolean
)
