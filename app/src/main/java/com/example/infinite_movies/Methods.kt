package com.example.infinite_movies

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat

fun isPasswordLongEnough(password: String): Boolean {
    return password.length > FIVE
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun doPasswordsMatch(password: String, repeatPassword: String): Boolean {
    return password == repeatPassword
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = ContextCompat.getSystemService(context, ConnectivityManager::class.java)
    val activeNetworkInfo = connectivityManager?.activeNetwork
    return activeNetworkInfo != null
}