package com.example.infinite_movies.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinite_movies.database.ShowsDatabase
import com.example.infinite_movies.model.RegisterRequest
import com.example.infinite_movies.model.RegisterResponse
import com.example.infinite_movies.model.User
import com.example.infinite_movies.networking.ApiModule
import java.util.concurrent.Executors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(
    private val database: ShowsDatabase
) : ViewModel() {

    private val registrationResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getRegistrationResultLiveData(): LiveData<Boolean> {
        return registrationResultLiveData
    }

    fun onRegisterButtonClicked(username: String, password: String) {

        val registerRequest = RegisterRequest(
            email = username,
            password = password,
            passwordConfirmation = password
        )
        ApiModule.retrofit.register(registerRequest)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    registrationResultLiveData.value = response.isSuccessful
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    registrationResultLiveData.value = false
                }

            })
    }
}