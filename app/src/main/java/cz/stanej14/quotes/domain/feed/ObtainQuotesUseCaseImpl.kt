package cz.stanej14.quotes.domain.feed

import cz.stanej14.quotes.data.QuotesService
import cz.stanej14.quotes.data.QuotesService.Companion.TYPE_TAG
import cz.stanej14.quotes.domain.network.mapper.QuoteMapper
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class ObtainQuotesUseCaseImpl @Inject constructor(
    private val quotesService: QuotesService,
    private val mapper: QuoteMapper
) : ObtainQuotesUseCase {

    override suspend fun obtainQuotes(
        query: String?,
        shouldSearchByTag: Boolean
    ): Resource<List<Quote>> {
        return try {
            val data = query?.run {
                val type = TYPE_TAG.takeIf { shouldSearchByTag }
                quotesService.getQuotes(query = query, type = type).quotes
            } ?: quotesService.getQuotes().quotes
            Resource.Success(mapper.map(data))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}