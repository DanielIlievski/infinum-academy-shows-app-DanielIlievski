package com.example.infinite_movies.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowResponse(
    @SerialName("show") val show: Show,
)