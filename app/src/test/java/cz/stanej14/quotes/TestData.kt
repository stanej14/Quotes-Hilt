package cz.stanej14.quotes

import cz.stanej14.quotes.domain.network.quotes.QuoteDto
import cz.stanej14.quotes.domain.network.error.ServerError
import cz.stanej14.quotes.domain.network.session.SessionResponse
import cz.stanej14.quotes.model.Quote
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

object TestData {

    val serverError = ServerError(500, HttpException(Response.error<Any>(500, "".toResponseBody())))

    val quoteDto = QuoteDto(
        tags = listOf("tag1", "tag2"),
        author = "This is an author",
        body = "This is a body",
        userDetails = QuoteDto.UserDetailsDto(false),
        id = 10
    )

    val listOfQuoteDtos = listOf(
        quoteDto,
        quoteDto.copy(body = "1"),
        quoteDto.copy(body = "2"),
        quoteDto.copy(body = "3")
    )

    val quote = Quote(
        id = 1,
        tags = listOf("tag123", "tag212"),
        author = "This is an author quote",
        body = "This is a body quote",
        isFavorite = true
    )

    val listOfQuotes = listOf(
        quote,
        quote.copy(body = "1"),
        quote.copy(body = "2"),
        quote.copy(body = "3")
    )

    val sessionResponse = SessionResponse(
        token = "token",
        login = "my login",
        email = "my email"
    )
}