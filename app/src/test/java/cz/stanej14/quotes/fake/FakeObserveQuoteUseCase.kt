package cz.stanej14.quotes.fake

import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.domain.detail.ObserveQuoteUseCase
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeObserveQuoteUseCase : ObserveQuoteUseCase {

    private var quote: Quote? = null
    private var throwable: Throwable = TestData.serverError

    fun withSuccess(quote: Quote) {
        this.quote = quote
    }

    fun withError(throwable: Throwable) {
        this.throwable = throwable
    }

    override suspend fun observeQuote(quoteId: Long): Flow<Resource<Quote>> {
        return flow {
            val result: Resource<Quote> = if (quote != null) Resource.Success(quote!!)
            else Resource.Error(throwable)
            emit(result)
        }
    }
}
