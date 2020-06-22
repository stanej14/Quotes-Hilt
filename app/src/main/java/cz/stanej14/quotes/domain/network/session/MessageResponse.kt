package cz.stanej14.quotes.domain.network.session

import com.squareup.moshi.Json

data class MessageResponse(@Json(name = "message") val message: String)