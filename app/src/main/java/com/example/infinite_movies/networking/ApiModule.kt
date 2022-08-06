package com.example.infinite_movies.networking

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.infinite_movies.APPLICATION_JSON
import com.example.infinite_movies.AuthInterceptor
import com.example.infinite_movies.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ApiModule {

    lateinit var retrofit: ShowsApiService

    fun initRetrofit(context: Context) {

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory(APPLICATION_JSON.toMediaType()))
            .client(okhttpClient(context))
            .build()
            .create(ShowsApiService::class.java)
    }

    private fun okhttpClient(context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .addInterceptor(logging)
            .build()
    }
}