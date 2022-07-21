package com.example.infinite_movies.model

import android.widget.RatingBar
import androidx.annotation.DrawableRes

data class Review(
    val id: Int,
    val username: String,
    val comment: String,
    val ratingStars: Int,
    @DrawableRes val imageResourceId: Int
)