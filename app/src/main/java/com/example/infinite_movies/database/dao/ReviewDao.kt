package com.example.infinite_movies.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.infinite_movies.database.entity.ReviewEntity

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review")
    fun getAllReviews(): LiveData<List<ReviewEntity>>

    @Query("SELECT * FROM review WHERE id IS :reviewId")
    fun getReview(reviewId: String): LiveData<ReviewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReview(review: ReviewEntity)
}