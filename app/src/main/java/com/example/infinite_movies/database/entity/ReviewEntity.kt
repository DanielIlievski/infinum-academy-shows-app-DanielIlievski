package com.example.infinite_movies.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.infinite_movies.converter.UserTypeConverter
import com.example.infinite_movies.model.User

@Entity(tableName = "review")
//@TypeConverters(UserTypeConverter::class)
data class ReviewEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "comment") val comment: String,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "show_id") val showId: Int,
    @ColumnInfo(name = "user") val user: User
)