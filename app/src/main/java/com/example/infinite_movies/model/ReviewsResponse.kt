package com.example.infinite_movies.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewsResponse(
    @SerialName("reviews") val reviews: List<Review>,
    @SerialName("meta") val meta: Meta
)