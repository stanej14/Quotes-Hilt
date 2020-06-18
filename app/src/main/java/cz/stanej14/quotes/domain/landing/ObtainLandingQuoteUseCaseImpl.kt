package cz.stanej14.quotes.domain.landing

import cz.stanej14.quotes.data.QuotesService
import cz.stanej14.quotes.domain.network.mapper.QuoteMapper
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import java.lang.UnsupportedOperationException
import javax.inject.Inject

class ObtainLandingQuoteUseCaseImpl @Inject constructor(
    private val quotesService: QuotesService,
    private val mapper: QuoteMapper
) : ObtainLandingQuoteUseCase {
    override suspend fun obtainLandingQuote(): Resource<Quote> {
        return try {
            val data = quotesService.getQuoteOfTheDay().quote
            val quote = mapper.map(data)!!
            Resource.Success(quote)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}