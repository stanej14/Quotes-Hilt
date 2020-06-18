package cz.stanej14.quotes.domain.network.quotes

import com.squareup.moshi.Json

data class QuoteDto(
    @Json(name = "id") val id: Long,
    @Json(name = "tags") val tags: List<String>,
    @Json(name = "body") val body: String,
    @Json(name = "author") val author: String?,
    @Json(name = "user_details") val userDetails: UserDetailsDto?
) {
    data class UserDetailsDto(
        @Json(name = "favorite") val favorite: Boolean
    )
}