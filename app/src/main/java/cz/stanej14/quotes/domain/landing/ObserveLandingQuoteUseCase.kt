package cz.stanej14.quotes.domain.landing

import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.flow.Flow

interface ObserveLandingQuoteUseCase {
    suspend fun observeLandingQuote(): Flow<Resource<Quote>>
}