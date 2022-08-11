package com.example.infinite_movies

fun isPasswordLongEnough(password: String): Boolean {
    return password.length > FIVE
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun doPasswordsMatch(password: String, repeatPassword: String): Boolean {
    return password == repeatPassword
}