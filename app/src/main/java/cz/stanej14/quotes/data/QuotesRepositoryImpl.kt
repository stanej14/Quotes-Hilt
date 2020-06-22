package cz.stanej14.quotes.data

import cz.stanej14.quotes.domain.network.mapper.QuoteMapper
import cz.stanej14.quotes.domain.network.quotes.QuoteDto
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import cz.stanej14.quotes.model.data
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.channelFlow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@FlowPreview
@ExperimentalCoroutinesApi
class QuotesRepositoryImpl @Inject constructor(
    private val quotesService: QuotesService,
    private val mapper: QuoteMapper
) : QuotesRepository {

    private val quoteOfTheDayChannel = ConflatedBroadcastChannel<Resource<Quote>>()
    private val quotes = ConflatedBroadcastChannel<Resource<List<Quote>>>()
    private val quoteByIdChannel = ConflatedBroadcastChannel<Resource<Quote>>()

    private fun getQuotes(): List<Quote>? = quotes.valueOrNull?.data
    private fun getQuoteOfTheDay(): Quote? = quoteOfTheDayChannel.valueOrNull?.data
    private fun getQuoteById(): Quote? = quoteByIdChannel.valueOrNull?.data

    override suspend fun observeQuoteOfTheDay(): Flow<Resource<Quote>> {
        quoteOfTheDayChannel.offer(obtainQuoteOfTheDay())
        return quoteOfTheDayChannel.asFlow()
    }

    override suspend fun observeQuotes() = quotes.asFlow()

    override fun onQuoteFavoriteChange(quote: Quote) {
        if (quote == getQuoteOfTheDay()) {
            quoteOfTheDayChannel.offer(Resource.Success(quote))
        }

        if (quote == getQuoteById()) {
            quoteByIdChannel.offer(Resource.Success(quote))
        }

        getQuotes()?.run {
            if (contains(quote)) {
                quotes.offer(
                    Resource.Success(toMutableList()
                        .also {
                            val index = it.indexOf(quote)
                            it[index] = quote
                        })
                )
            }
        }
    }

    override suspend fun observeQuoteById(quoteId: Long): Flow<Resource<Quote>> {
        val quote = findQuoteById(quoteId)?.let { Resource.Success(it) } ?: obtainQuoteById(quoteId)
        quoteByIdChannel.offer(quote)

        return quoteByIdChannel.asFlow()
    }

    private fun findQuoteById(quoteId: Long): Quote? {
        return getQuotes()?.firstOrNull { it.id == quoteId } ?: kotlin.run {
            getQuoteOfTheDay()?.takeIf { it.id == quoteId }
        }
    }

    private suspend fun obtainQuoteById(quoteId: Long) =
        obtainQuote { quotesService.getQuote(quoteId) }

    private suspend fun obtainQuoteOfTheDay() =
        obtainQuote { quotesService.getQuoteOfTheDay().quote }


    private suspend fun obtainQuote(obtainFun: suspend () -> QuoteDto): Resource<Quote> {
        return try {
            val data = obtainFun()
            val quote = mapper.map(data)!!
            Resource.Success(quote)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}