package com.example.infinite_movies.viewModel

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinite_movies.database.ShowsDatabase
import com.example.infinite_movies.database.entity.ReviewEntity
import com.example.infinite_movies.database.entity.ShowEntity
import com.example.infinite_movies.errorAlertDialog
import com.example.infinite_movies.model.Review
import com.example.infinite_movies.model.ReviewRequest
import com.example.infinite_movies.model.ReviewResponse
import com.example.infinite_movies.model.ReviewsResponse
import com.example.infinite_movies.model.Show
import com.example.infinite_movies.model.ShowResponse
import com.example.infinite_movies.networking.ApiModule
import java.util.concurrent.Executors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel(
    private val database: ShowsDatabase
) : ViewModel() {

    private val _reviewsLiveData = MutableLiveData<List<Review>>()
    val reviewsLiveData: LiveData<List<Review>> = _reviewsLiveData

    private val _progressBarLiveData = MutableLiveData(View.VISIBLE)
    val progressBarLiveData: LiveData<Int> = _progressBarLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun reviewListToReviewEntityList(reviewList: List<Review>?): List<ReviewEntity>? {
        return reviewList?.map { review ->
            ReviewEntity(review.id, review.comment, review.rating, review.showId, review.user)
        }
    }

    fun fetchReviewsFromApi(showId: Int) {
        ApiModule.retrofit.fetchReviews(showId)
            .enqueue(object : Callback<ReviewsResponse> {
                override fun onResponse(call: Call<ReviewsResponse>, response: Response<ReviewsResponse>) {
                    when (response.code()) {
                        200 -> {
                            _reviewsLiveData.value = response.body()?.reviews
                            _progressBarLiveData.value = View.GONE

                            Executors.newSingleThreadExecutor().execute {
                                reviewListToReviewEntityList(response.body()?.reviews)?.let { reviewEntityList ->
                                    database.reviewDao().insertAllReviews(reviewEntityList)
                                }
                            }
                        }
                        401 -> {
                            _errorLiveData.value = "You need to sign in or sign up before continuing."
                        }
                        404 -> {
                            _errorLiveData.value = "Couldn't find Show with 'id'=$showId"
                        }
                    }
                }

                override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                }

            })
    }

    fun fetchReviewsFromDatabase(showId: Int): LiveData<List<ReviewEntity>> {

        return database.reviewDao().getAllReviews(showId)
    }

    private val _reviewAdd = MutableLiveData<Review>()
    val reviewAdd: LiveData<Review> = _reviewAdd

    fun createReview(reviewRequest: ReviewRequest) {
        ApiModule.retrofit.createReview(reviewRequest)
            .enqueue(object : Callback<ReviewResponse> {
                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                    when (response.code()) {
                        200 -> {
                            _reviewAdd.value = response.body()?.review
                        }
                        401 -> {
                            _errorLiveData.value = "You need to sign in or sign up before continuing."
                        }
                        422 -> {
                            _errorLiveData.value = "Show must exist"
                        }
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                }

            })
    }

    private val _singleShowLiveData = MutableLiveData<Show>()
    val singleShowLiveData: LiveData<Show> = _singleShowLiveData

    fun fetchShowFromApi(showId: Int) {
        ApiModule.retrofit.fetchShow(showId)
            .enqueue(object : Callback<ShowResponse> {
                override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                    when (response.code()) {
                        200 -> {
                            _singleShowLiveData.value = response.body()?.show
                        }
                        401 -> {
                            _errorLiveData.value = "You need to sign in or sign up before continuing."
                        }
                        404 -> {
                            _errorLiveData.value = "Couldn't find Show with 'id'=$showId"
                        }
                    }
                }

                override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                }

            })
    }

    fun fetchShowFromDatabase(showId: Int): LiveData<ShowEntity> {
        return database.showDao().getShow(showId)
    }
}