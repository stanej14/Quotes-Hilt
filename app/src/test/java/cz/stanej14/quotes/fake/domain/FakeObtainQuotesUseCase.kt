package cz.stanej14.quotes.fake.domain

import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.domain.feed.ObtainQuotesUseCase
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource

class FakeObtainQuotesUseCase : ObtainQuotesUseCase {

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

    override suspend fun obtainQuotes(
        query: String?,
        shouldSearchByTag: Boolean
    ): Resource<List<Quote>> {
        calls++
        quotes?.run {
            usedQuery = query
            usedSearchByTag = shouldSearchByTag
            return Resource.Success(this)
        }
        return Resource.Error(throwable)
    }
}