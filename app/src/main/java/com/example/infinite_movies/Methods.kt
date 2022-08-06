package com.example.infinite_movies

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.infinite_movies.fragment.ShowsFragmentDirections

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

fun errorAlertDialog(context: Context, message: String) {
    val builder = AlertDialog.Builder(context)

    builder.apply {
        setTitle("Error")
        setMessage(message)
        setIcon(android.R.drawable.ic_dialog_alert)
        setCancelable(false)
        setNeutralButton("Close") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
    }
    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()
}