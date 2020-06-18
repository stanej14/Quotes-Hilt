package cz.stanej14.quotes.fake.domain

import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.domain.landing.ObtainLandingQuoteUseCase
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource

class FakeObtainLandingQuoteUseCase : ObtainLandingQuoteUseCase {

    private var quote: Quote? = null
    private var throwable: Throwable = TestData.serverError

    fun withSuccess(quote: Quote) {
        this.quote = quote
    }

    fun withError(throwable: Throwable) {
        this.throwable = throwable
    }

    override suspend fun obtainLandingQuote(): Resource<Quote> {
        quote?.run { return Resource.Success(this) }
        return Resource.Error(throwable)
    }
}