package com.example.infinite_movies

import android.content.Context

class SessionManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(LOGIN, Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        sharedPreferences
            .edit()
            .putString(ACCESS_TOKEN, token)
            .apply()
    }

    fun saveClient(client: String) {
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
        sharedPreferences.edit().remove(ACCESS_TOKEN).apply()
        sharedPreferences.edit().remove(CLIENT).apply()
        sharedPreferences.edit().remove(UID).apply()
    }

    fun fetchAuthToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, null)
    }

    fun fetchClient(): String? {
        return sharedPreferences.getString(CLIENT, null)
    }

    fun fetchUid(): String? {
        return sharedPreferences.getString(UID, null)
    }
}