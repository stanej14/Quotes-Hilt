package cz.stanej14.quotes.domain.network.session

import com.squareup.moshi.Json

data class CreateSessionRequestBody(@Json(name = "user") val user: UserDto) {

    data class UserDto(
        @Json(name = "login") val login: String,
        @Json(name = "password") val password: String
    )
}