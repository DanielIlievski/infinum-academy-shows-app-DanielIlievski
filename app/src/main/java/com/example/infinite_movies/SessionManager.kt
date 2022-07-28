package com.example.infinite_movies

import android.content.Context
import androidx.core.content.edit

private const val USER_TOKEN = "USER_TOKEN"
private const val UID = "UID"
private const val CLIENT = "CLIENT"

class SessionManager (context: Context) {

    private val sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE)

    fun saveAuthToken (token: String) {
        sharedPreferences
            .edit()
            .putString(USER_TOKEN, token)
            .apply()
    }

    fun saveClient (client: String) {
        sharedPreferences
            .edit()
            .putString(CLIENT, client)
            .apply()
    }

    fun saveUid(uid: String) {
        sharedPreferences
            .edit()
            .putString(UID, uid)
            .apply()
    }

    fun deleteHeaders() {
        sharedPreferences.edit().remove(USER_TOKEN).apply()
        sharedPreferences.edit().remove(CLIENT).apply()
        sharedPreferences.edit().remove(UID).apply()
    }

    fun fetchAuthToken (): String? {
        return sharedPreferences.getString(USER_TOKEN, null)
    }

    fun fetchClient (): String? {
        return sharedPreferences.getString(CLIENT, null)
    }

    fun fetchUid (): String? {
        return sharedPreferences.getString(UID, null)
    }
}