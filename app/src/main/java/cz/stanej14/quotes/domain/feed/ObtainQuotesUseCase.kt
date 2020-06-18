package cz.stanej14.quotes.domain.feed

import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource

interface ObtainQuotesUseCase {
    suspend fun obtainQuotes(
        query: String? = null,
        shouldSearchByTag: Boolean = false
    ): Resource<List<Quote>>
}