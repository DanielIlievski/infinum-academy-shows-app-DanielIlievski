package com.example.infinite_movies.model

import android.net.Uri
import androidx.annotation.DrawableRes

data class Review(
    val id: Int,
    val username: String,
    val comment: String,
    val ratingStars: Int,
    val profilePhotoUri: Uri
)