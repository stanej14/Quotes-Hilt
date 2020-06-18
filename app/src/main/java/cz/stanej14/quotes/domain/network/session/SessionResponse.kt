package cz.stanej14.quotes.domain.network.session

import com.squareup.moshi.Json

data class SessionResponse(
    @Json(name = "User-Token") val token: String,
    @Json(name = "login") val login: String,
    @Json(name = "email") val email: String
)