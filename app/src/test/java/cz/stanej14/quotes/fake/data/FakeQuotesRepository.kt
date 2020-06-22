package cz.stanej14.quotes.fake.data

import cz.stanej14.quotes.data.QuotesRepository
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class FakeQuotesRepository : QuotesRepository {

    private var throwable: Throwable? = null
    val quoteOfTheDayChannel = ConflatedBroadcastChannel<Quote>()

    val quoteByIdChannel = ConflatedBroadcastChannel<Quote>()

    fun shouldFail(_throwable: Throwable = RuntimeException()) {
        throwable = _throwable
    }

    @FlowPreview
    override suspend fun observeQuoteOfTheDay(): Flow<Resource<Quote>> {
        return quoteOfTheDayChannel.asFlow().map {
            if (throwable != null) {
                Resource.Error(throwable!!)
            } else {
                Resource.Success(it)
            }
        }
    }

    override suspend fun observeQuotes(): Flow<Resource<List<Quote>>> {
        TODO("Not yet implemented")
    }

    override suspend fun observeQuoteById(quoteId: Long): Flow<Resource<Quote>> {
        return quoteByIdChannel.asFlow().map {
            if (throwable != null) {
                Resource.Error(throwable!!)
            } else {
                Resource.Success(it)
            }
        }
    }

    override fun onQuoteFavoriteChange(quote: Quote) {
        TODO("Not yet implemented")
    }
}