package cz.stanej14.quotes.data

import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.flow.Flow

interface QuotesRepository {
    suspend fun observeQuoteOfTheDay(): Flow<Resource<Quote>>
    suspend fun observeQuotes(): Flow<Resource<List<Quote>>>
    suspend fun observeQuoteById(quoteId: Long): Flow<Resource<Quote>>
    fun onQuoteFavoriteChange(quote: Quote)
}