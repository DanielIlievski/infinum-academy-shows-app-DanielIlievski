package com.example.infinite_movies.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "show")
data class ShowEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: String,
    @ColumnInfo(name = "average_rating") val avgRating: Float?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "image") val imgUrl: String,
    @ColumnInfo(name = "number_of_reviews") val numberOfReviews: Int = 0,
    @ColumnInfo(name = "title") val title: String
)