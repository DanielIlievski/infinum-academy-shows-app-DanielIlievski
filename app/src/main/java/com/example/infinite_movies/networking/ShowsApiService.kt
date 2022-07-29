package com.example.infinite_movies.networking

import com.example.infinite_movies.model.LoginRequest
import com.example.infinite_movies.model.LoginResponse
import com.example.infinite_movies.model.RegisterRequest
import com.example.infinite_movies.model.RegisterResponse
import com.example.infinite_movies.model.ShowsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// TODO define all functions required to communicate with the server
interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/shows")
    fun fetchShows(): Call<ShowsResponse>

    //    @GET("/shows/{id}")
    //    fun fetchShow(@Path("id") showId: Int): Call<Show>

//    @GET("/shows/{show_id}/reviews")
//    fun fetchReviews(@Path("show_id") showId: Int): Call<ReviewsResponse>

    //    @GET("/reviews")
    //    fun createReview(@Body request: ReviewRequest): Call<Review>
}