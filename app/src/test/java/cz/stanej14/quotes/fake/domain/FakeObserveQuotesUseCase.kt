package cz.stanej14.quotes.fake.domain

import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.domain.feed.ObserveQuotesUseCase
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeObserveQuotesUseCase : ObserveQuotesUseCase {

    private var quotes: List<Quote>? = null
    private var throwable: Throwable = TestData.serverError
    var calls: Int = 0
        private set
    var usedQuery: String? = null
    var usedSearchByTag: Boolean? = null

    fun resetCount() {
        calls = 0
    }

    fun withSuccess(quotes: List<Quote>) {
        this.quotes = quotes
    }

    fun withError(throwable: Throwable) {
        this.throwable = throwable
    }

    override suspend fun observeQuotes(
        query: String?,
        shouldSearchByTag: Boolean
    ): Flow<Resource<List<Quote>>> {
        calls++
        usedQuery = query
        usedSearchByTag = shouldSearchByTag
        return flow {
            val result: Resource<List<Quote>> = if (quotes != null) Resource.Success(quotes!!)
            else Resource.Error(throwable)
            emit(result)
        }
    }
}