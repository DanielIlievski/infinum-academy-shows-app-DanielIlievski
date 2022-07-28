package com.example.infinite_movies.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest (
    @SerialName("email")
    var email: String,

    @SerialName("password")
    var password: String
)