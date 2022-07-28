package com.example.infinite_movies.model

import androidx.annotation.DrawableRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Show (
    @SerialName("id") val id: Int,
    @SerialName("average_rating") val avgRating: Float?,
    @SerialName("description") val description: String?,
    @SerialName("image_url") val imgUrl: String,
    @SerialName("no_of_reviews") val numberOfReviews: Int = 0,
    @SerialName("title") val title: String
)