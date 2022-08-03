package com.example.infinite_movies.converter

import androidx.room.TypeConverter
import com.example.infinite_movies.model.User
import com.google.gson.Gson

class UserTypeConverter {

    @TypeConverter
    fun stringToUser(string: String): User = Gson().fromJson(string, User::class.java)

    @TypeConverter
    fun userToString(user: User): String = Gson().toJson(user)
}