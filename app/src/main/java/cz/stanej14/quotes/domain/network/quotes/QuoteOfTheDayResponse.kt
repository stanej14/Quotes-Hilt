package cz.stanej14.quotes.domain.network.quotes

import com.squareup.moshi.Json
import cz.stanej14.quotes.domain.network.quotes.QuoteDto

data class QuoteOfTheDayResponse(
    @Json(name = "quote") val quote: QuoteDto
)