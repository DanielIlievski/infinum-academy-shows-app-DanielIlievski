package com.example.infinite_movies.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowsResponse(
    @SerialName("shows") val shows: List<Show>,
    @SerialName("meta") val meta: Meta
)

@Serializable
data class Meta(
    @SerialName("pagination") val pagination: Pagination
)

@Serializable
data class Pagination(
    @SerialName("count") val count: Long,
    @SerialName("page") val page: Long,
    @SerialName("items") val items: Long,
    @SerialName("pages") val pages: Long
)