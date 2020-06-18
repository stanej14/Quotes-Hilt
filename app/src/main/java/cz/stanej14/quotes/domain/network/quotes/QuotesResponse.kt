package cz.stanej14.quotes.domain.network.quotes

import com.squareup.moshi.Json

data class QuotesResponse(
    @Json(name = "page") val page: Int,
    @Json(name = "last_page") val lastPage: Boolean,
    @Json(name = "quotes") val quotes: List<QuoteDto>
)