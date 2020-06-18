package cz.stanej14.quotes.domain.favorite

import cz.stanej14.quotes.data.QuotesService
import cz.stanej14.quotes.domain.network.mapper.QuoteMapper
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.CancellationException
import java.lang.Exception
import javax.inject.Inject

class ToggleFavoriteQuoteUseCaseImpl @Inject constructor(
    private val quotesService: QuotesService,
    private val quoteMapper: QuoteMapper
) : ToggleFavoriteQuoteUseCase {

    override suspend fun toggleFavorite(quote: Quote): Resource<Quote> {
        return try {
            val data = if (quote.isFavorite) {
                quotesService.unfavoriteQuote(quote.id)
            } else {
                quotesService.favoriteQuote(quote.id)
            }
            Resource.Success(quoteMapper.map(data)!!)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}