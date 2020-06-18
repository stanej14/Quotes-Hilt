package cz.stanej14.quotes.domain.landing

import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource

interface ObtainLandingQuoteUseCase {
    suspend fun obtainLandingQuote(): Resource<Quote>
}