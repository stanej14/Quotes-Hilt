package cz.stanej14.quotes.domain.detail

import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.flow.Flow

interface ObserveQuoteUseCase {
    suspend fun observeQuote(quoteId: Long): Flow<Resource<Quote>>
}