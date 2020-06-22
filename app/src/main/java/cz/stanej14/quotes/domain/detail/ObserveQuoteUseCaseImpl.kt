package cz.stanej14.quotes.domain.detail

import cz.stanej14.quotes.data.QuotesRepository
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ObserveQuoteUseCaseImpl @Inject constructor(
    private val quotesRepository: QuotesRepository
) : ObserveQuoteUseCase {

    override suspend fun observeQuote(quoteId: Long): Flow<Resource<Quote>> {
        return quotesRepository.observeQuoteById(quoteId)
    }
}