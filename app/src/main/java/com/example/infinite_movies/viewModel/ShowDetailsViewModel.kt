package com.example.infinite_movies.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinite_movies.R
import com.example.infinite_movies.model.Review

class ShowDetailsViewModel : ViewModel() {

    private var reviews = listOf(
        Review(
            1,
            "daniel.ilievski",
            "Great show!",
            5,
            R.drawable.ic_review_profile
        ),
        Review(
            2,
            "petar.petrovski",
            "",
            2,
            R.drawable.ic_review_profile
        ),
        Review(
            3,
            "marko.markoski",
            "I laughed so much!",
            4,
            R.drawable.ic_review_profile
        ),
        Review(
            4,
            "ivan.ivanovski",
            "Relaxing show.",
            4,
            R.drawable.ic_review_profile
        ),
        Review(
            5,
            "andrej.krsteski",
            "It was ok.",
            3,
            R.drawable.ic_review_profile
        ),
        Review(
            6,
            "janko.stojanovski",
            "",
            1,
            R.drawable.ic_review_profile
        ),
        Review(
            7,
            "martin.stojceski",
            "",
            5,
            R.drawable.ic_review_profile
        ),
        Review(
            8,
            "viktor.smilevski",
            "Loved it!",
            4,
            R.drawable.ic_review_profile
        ),
        Review(
            9,
            "stefan.dimeski",
            "I like it.",
            3,
            R.drawable.ic_review_profile
        ),
        Review(
            10,
            "petko.petkoski",
            "",
            3,
            R.drawable.ic_review_profile
        )
    )

    private val _reviewsLiveData = MutableLiveData<List<Review>>()
    val reviewsLiveData : LiveData<List<Review>> = _reviewsLiveData

    private val _ratingBarRating = MutableLiveData<Float>()
    val ratingBarRating: LiveData<Float> = _ratingBarRating

    private val _ratingBarText = MutableLiveData<String>()
    val ratingBarText: LiveData<String> = _ratingBarText

    private val _reviewAdd = MutableLiveData<Review>()
    val reviewAdd : LiveData<Review> = _reviewAdd

    init {
        _reviewsLiveData.value = reviews
    }

    private fun getAvgRatingStars() : Float {
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

    fun addReviewToList(username: String, comment: String, numStars: Int) {
        _reviewAdd.value = Review(reviews.count()+1, username, comment, numStars, R.drawable.ic_review_profile)

        reviews = reviews + _reviewAdd.value!!

        addRatingBarStats()
    }
}