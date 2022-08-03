package com.example.infinite_movies.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.infinite_movies.converter.UserTypeConverter
import com.example.infinite_movies.database.entity.ReviewEntity

@Dao
//@TypeConverters(UserTypeConverter::class)
interface ReviewDao {

    @Query("SELECT * FROM review WHERE show_id IS :showId")
    fun getAllReviews(showId: Int): LiveData<List<ReviewEntity>>

    @Query("SELECT * FROM review WHERE id IS :reviewId")
    fun getReview(reviewId: Int): LiveData<ReviewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllReviews(reviews: List<ReviewEntity>)
}