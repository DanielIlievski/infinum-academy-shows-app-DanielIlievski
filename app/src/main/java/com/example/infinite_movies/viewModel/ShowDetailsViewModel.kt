package com.example.infinite_movies.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinite_movies.model.Review
import com.example.infinite_movies.model.ReviewRequest
import com.example.infinite_movies.model.ReviewResponse
import com.example.infinite_movies.model.ReviewsResponse
import com.example.infinite_movies.model.Show
import com.example.infinite_movies.model.ShowResponse
import com.example.infinite_movies.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel : ViewModel() {

    private val _reviewsLiveData = MutableLiveData<List<Review>>()
    val reviewsLiveData: LiveData<List<Review>> = _reviewsLiveData

    fun fetchReviews(showId: Int) {
        ApiModule.retrofit.fetchReviews(showId)
            .enqueue(object : Callback<ReviewsResponse> {
                override fun onResponse(call: Call<ReviewsResponse>, response: Response<ReviewsResponse>) {
                    if (response.code() == 200) {
                        _reviewsLiveData.value = response.body()?.reviews
                    }
                }

                override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                }

            })
    }

    private val _reviewAdd = MutableLiveData<Review>()
    val reviewAdd: LiveData<Review> = _reviewAdd

    fun createReview(reviewRequest: ReviewRequest) {
        ApiModule.retrofit.createReview(reviewRequest)
            .enqueue(object : Callback<ReviewResponse> {
                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                    if (response.code() == 201) {
                        _reviewAdd.value = response.body()?.review
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                }

            })
    }

    private val _singleShowLiveData = MutableLiveData<Show>()
    val singleShowLiveData: LiveData<Show> = _singleShowLiveData

    fun fetchShow(showId: Int) {
        ApiModule.retrofit.fetchShow(showId)
            .enqueue(object : Callback<ShowResponse> {
                override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                    if (response.code() == 200) {
                        _singleShowLiveData.value = response.body()?.show
                    }
                }

                override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                }

            })
    }
}