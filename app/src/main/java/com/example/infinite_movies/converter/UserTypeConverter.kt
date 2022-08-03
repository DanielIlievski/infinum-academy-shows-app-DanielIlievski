package com.example.infinite_movies.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.Room
import androidx.room.TypeConverter
import com.example.infinite_movies.model.User
import com.example.infinite_movies.util.JsonParser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//@ProvidedTypeConverter
class UserTypeConverter {

    @TypeConverter
    fun stringToUser(string: String): User = Gson().fromJson(string, User::class.java)

    @TypeConverter
    fun userToString(user: User): String = Gson().toJson(user)

//    @TypeConverter
//    fun toUserJson(user: User): String {
//        return jsonParser.toJson(
//            user,
//            object : TypeToken<ArrayList<User>>() {}.type
//        ) ?: ""
//    }
//
//    @TypeConverter
//    fun fromUserJson(json: String): User? {
//        return jsonParser.fromJson<User>(
//            json,
//            object: TypeToken<ArrayList<User>>(){}.type
//        )
//    }
}