package cz.stanej14.quotes.domain.landing

import cz.stanej14.quotes.data.QuotesRepository
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ObserveLandingQuoteUseCaseImpl @Inject constructor(
    private val quotesRepository: QuotesRepository
) : ObserveLandingQuoteUseCase {

    override suspend fun observeLandingQuote(): Flow<Resource<Quote>> {
        return quotesRepository.observeQuoteOfTheDay()
    }
}