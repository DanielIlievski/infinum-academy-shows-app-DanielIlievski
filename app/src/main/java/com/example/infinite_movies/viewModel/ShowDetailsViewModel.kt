package com.example.infinite_movies.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinite_movies.R
import com.example.infinite_movies.model.Review

class ShowDetailsViewModel : ViewModel() {

    val imageUri: Uri = Uri.parse("android.resource://com.example.infinite_movies/" + R.drawable.ic_review_profile)

    private var reviews = listOf(
        Review(
            1,
            "daniel.ilievski",
            "Great show!",
            5,
            imageUri
        ),
        Review(
            2,
            "petar.petrovski",
            "",
            2,
            imageUri
        ),
        Review(
            3,
            "marko.markoski",
            "I laughed so much!",
            4,
            imageUri
        ),
        Review(
            4,
            "ivan.ivanovski",
            "Relaxing show.",
            4,
            imageUri
        ),
        Review(
            5,
            "andrej.krsteski",
            "It was ok.",
            3,
            imageUri
        ),
        Review(
            6,
            "janko.stojanovski",
            "",
            1,
            imageUri
        ),
        Review(
            7,
            "martin.stojceski",
            "",
            5,
            imageUri
        ),
        Review(
            8,
            "viktor.smilevski",
            "Loved it!",
            4,
            imageUri
        ),
        Review(
            9,
            "stefan.dimeski",
            "I like it.",
            3,
            imageUri
        ),
        Review(
            10,
            "petko.petkoski",
            "",
            3,
            imageUri
        )
    )

    private val _reviewsLiveData = MutableLiveData<List<Review>>()
    val reviewsLiveData: LiveData<List<Review>> = _reviewsLiveData

    private val _ratingBarRating = MutableLiveData<Float>()
    val ratingBarRating: LiveData<Float> = _ratingBarRating

    private val _ratingBarText = MutableLiveData<String>()
    val ratingBarText: LiveData<String> = _ratingBarText

    private val _reviewAdd = MutableLiveData<Review>()
    val reviewAdd: LiveData<Review> = _reviewAdd

    init {
        _reviewsLiveData.value = reviews
    }

    private fun getAvgRatingStars(): Float {
        var stars = 0
        for (review in reviews) {
            stars += review.ratingStars
        }
        return stars.toFloat() / reviews.count()
    }

    fun addRatingBarStats() {
        _ratingBarRating.value = getAvgRatingStars()

        _ratingBarText.value = String.format("%d reviews, %.2f average", reviews.count(), getAvgRatingStars())
    }

    fun addReviewToList(username: String, comment: String, numStars: Int, profilePhotoUri: Uri) {
        _reviewAdd.value = Review(reviews.count() + 1, username, comment, numStars, profilePhotoUri)

        reviews = reviews + _reviewAdd.value!!

        addRatingBarStats()
    }
}