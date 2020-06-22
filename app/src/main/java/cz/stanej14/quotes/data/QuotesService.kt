package cz.stanej14.quotes.data

import cz.stanej14.quotes.domain.network.quotes.QuoteDto
import cz.stanej14.quotes.domain.network.quotes.QuoteOfTheDayResponse
import cz.stanej14.quotes.domain.network.quotes.QuotesResponse
import cz.stanej14.quotes.domain.network.session.CreateSessionRequestBody
import cz.stanej14.quotes.domain.network.session.MessageResponse
import cz.stanej14.quotes.domain.network.session.SessionResponse
import retrofit2.http.*

interface QuotesService {

    companion object {
        const val TYPE_TAG = "tag"
    }

    @GET("qotd")
    suspend fun getQuoteOfTheDay(): QuoteOfTheDayResponse

    @GET("quotes/{quoteId}")
    suspend fun getQuote(@Path("quoteId") quoteId: Long): QuoteDto

    @GET("quotes")
    suspend fun getQuotes(
        @Query("filter") query: String? = null,
        @Query("type") type: String? = null
    ): QuotesResponse

    @POST("session")
    suspend fun createUserSession(@Body requestBody: CreateSessionRequestBody): SessionResponse

    @DELETE("session")
    suspend fun deleteUserSession(): MessageResponse

    @PUT("quotes/{quoteId}/fav")
    suspend fun favoriteQuote(@Path("quoteId") quoteId: Long): QuoteDto

    @PUT("quotes/{quoteId}/unfav")
    suspend fun unfavoriteQuote(@Path("quoteId") quoteId: Long): QuoteDto
}

class FakeQuotesService : QuotesService {

    lateinit var quoteOfTheDay: QuoteDto
    lateinit var favoriteQuoteDto: QuoteDto
    lateinit var unfavoriteQuoteDto: QuoteDto
    lateinit var quotes: List<QuoteDto>
    var throwable: Throwable? = null

    var usedType: String? = null
    var usedFilter: String? = null

    override suspend fun getQuoteOfTheDay(): QuoteOfTheDayResponse {
        throwable?.run { throw this }
        return QuoteOfTheDayResponse(
            quoteOfTheDay
        )
    }

    override suspend fun getQuote(quoteId: Long): QuoteDto {
        TODO("Not yet implemented")
    }

    override suspend fun getQuotes(
        @Query("filter") query: String?,
        @Query("type") type: String?
    ): QuotesResponse {
        throwable?.run { throw this }
        usedFilter = query
        usedType = type
        return QuotesResponse(
            0,
            false,
            quotes
        )
    }

    override suspend fun createUserSession(requestBody: CreateSessionRequestBody): SessionResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserSession(): MessageResponse {
        TODO("Not yet implemented")
    }

    override suspend fun favoriteQuote(quoteId: Long): QuoteDto {
        throwable?.run { throw this }
        return favoriteQuoteDto
    }

    override suspend fun unfavoriteQuote(quoteId: Long): QuoteDto {
        throwable?.run { throw this }
        return unfavoriteQuoteDto
    }
}