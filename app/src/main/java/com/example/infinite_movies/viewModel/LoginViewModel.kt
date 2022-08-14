package com.example.infinite_movies.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinite_movies.ACCESS_TOKEN
import com.example.infinite_movies.CLIENT
import com.example.infinite_movies.UID
import com.example.infinite_movies.model.LoginRequest
import com.example.infinite_movies.model.LoginResponse
import com.example.infinite_movies.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _accessTokenLiveData = MutableLiveData<String>()
    val accessTokenLiveData: LiveData<String> = _accessTokenLiveData

    private val _clientLiveData = MutableLiveData<String>()
    val clientLiveData: LiveData<String> = _clientLiveData

    private val _uidLiveData = MutableLiveData<String>()
    val uidLiveData: LiveData<String> = _uidLiveData

    private val _responseCodeLiveData = MutableLiveData<Int>()
    val responseCodeLiveData: LiveData<Int> = _responseCodeLiveData

    fun login(email: String, password: String) {

        val loginRequest = LoginRequest(
            email = email,
            password = password
        )
        ApiModule.retrofit.login(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    _accessTokenLiveData.value = response.headers()[ACCESS_TOKEN].toString()
                    _clientLiveData.value = response.headers()[CLIENT].toString()
                    _uidLiveData.value = response.headers()[UID].toString()
                    _responseCodeLiveData.value = response.code()
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                }

            })
    }
}