package cz.stanej14.quotes.domain.feed

import cz.stanej14.quotes.data.QuotesRepository
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveQuotesUseCaseImpl @Inject constructor(private val quotesRepository: QuotesRepository) :
    ObserveQuotesUseCase {

    override suspend fun observeQuotes(
        query: String?,
        shouldSearchByTag: Boolean
    ): Flow<Resource<List<Quote>>> {
        return quotesRepository.observeQuotes(query, shouldSearchByTag)
    }
}