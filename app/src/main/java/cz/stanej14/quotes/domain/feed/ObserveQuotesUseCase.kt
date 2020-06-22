package cz.stanej14.quotes.domain.feed

import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.flow.Flow

interface ObserveQuotesUseCase {
    suspend fun observeQuotes(
        query: String? = null,
        shouldSearchByTag: Boolean = false
    ): Flow<Resource<List<Quote>>>
}