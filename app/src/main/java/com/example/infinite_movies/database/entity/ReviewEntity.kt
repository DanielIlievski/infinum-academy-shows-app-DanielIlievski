package com.example.infinite_movies.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.infinite_movies.model.User

@Entity(tableName = "review")
data class ReviewEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: String,
    @ColumnInfo(name = "comment") val comment: String,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "show_id") val showId: Int,
    @ColumnInfo(name = "user") val user: User
)