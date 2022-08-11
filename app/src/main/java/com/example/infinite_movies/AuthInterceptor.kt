package com.example.infinite_movies

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {

    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionManager.fetchAuthToken()?.let { userToken ->
            requestBuilder.addHeader(ACCESS_TOKEN, value = userToken)
        }

        sessionManager.fetchClient()?.let { client ->
            requestBuilder.addHeader(CLIENT, client)
        }

        sessionManager.fetchUid()?.let { uid ->
            requestBuilder.addHeader(UID, uid)
        }

        return chain.proceed(requestBuilder.build())
    }
}