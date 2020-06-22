package cz.stanej14.quotes.fake.domain

import cz.stanej14.quotes.domain.detail.ToggleFavoriteQuoteUseCase
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import java.lang.RuntimeException

class FakeToggleFavoriteQuoteUseCase : ToggleFavoriteQuoteUseCase {

    private lateinit var result: Resource<Unit>

    fun withSuccess() {
        result = Resource.Success(Unit)
    }

    fun withError(throwable: Throwable = RuntimeException()) {
        result = Resource.Error(throwable)
    }

    override suspend fun toggleFavorite(quote: Quote) = result
}
