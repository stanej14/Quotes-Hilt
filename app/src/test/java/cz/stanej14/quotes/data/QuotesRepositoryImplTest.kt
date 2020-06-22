package cz.stanej14.quotes.data

import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.domain.network.mapper.QuoteMapper
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import cz.stanej14.quotes.model.data
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@FlowPreview
class QuotesRepositoryImplTest {

    private lateinit var fakeQuotesService: FakeQuotesService
    private val quotesMapper = QuoteMapper()
    private lateinit var underTest: QuotesRepositoryImpl

    @Before
    fun setUp() {
        fakeQuotesService = FakeQuotesService()
        underTest = QuotesRepositoryImpl(fakeQuotesService, quotesMapper)
    }

    @Test
    fun `Should return quote of the day properly`() {
        val data = TestData.quoteDto
        fakeQuotesService.quoteOfTheDay = data
        runBlockingTest {
            val result = underTest.observeQuoteOfTheDay().first().data!!

            assertEquals(data.tags, result.tags)
            assertEquals(data.author, result.author)
            assertEquals(data.body, result.body)
        }
    }

    @Test
    fun `Should fail obtaining a quote of the day gracefully`() {
        fakeQuotesService.throwable = TestData.serverError
        runBlockingTest {
            val result = underTest.observeQuoteOfTheDay().first()

            assertNull(fakeQuotesService.usedType)
            assertNull(fakeQuotesService.usedFilter)
            assertThat(result, instanceOf(Resource.Error::class.java))
        }
    }

    @Test
    fun `Should fail obtaining quotes gracefully`() {
        fakeQuotesService.throwable = TestData.serverError
        runBlockingTest {
            val result = underTest.observeQuotes().first()
            assertNull(fakeQuotesService.usedType)
            assertNull(fakeQuotesService.usedFilter)
            assertThat(result, instanceOf(Resource.Error::class.java))
        }
    }

    @Test
    fun `Should return list of quotes properly`() {
        val data = TestData.listOfQuoteDtos
        fakeQuotesService.quotes = data
        runBlockingTest {
            val result = underTest.observeQuotes().first() as Resource.Success<List<Quote>>

            assertNull(fakeQuotesService.usedType)
            assertNull(fakeQuotesService.usedFilter)
            assertEquals(data.size, result.data.size)
        }
    }

    @Test
    fun `Should search for quotes with query properly`() {
        val data = TestData.listOfQuoteDtos
        val query = "m_query"
        fakeQuotesService.quotes = data
        runBlockingTest {
            val result = underTest.observeQuotes(query = query).first() as Resource.Success<List<Quote>>

            assertNull(fakeQuotesService.usedType)
            assertEquals(query, fakeQuotesService.usedFilter)
            assertEquals(data.size, result.data.size)
        }
    }

    @Test
    fun `Should search for quotes with tag properly`() {
        val data = TestData.listOfQuoteDtos
        val query = "m_query"
        fakeQuotesService.quotes = data
        runBlockingTest {
            val result = underTest.observeQuotes(
                query = query,
                shouldSearchByTag = true
            ).first() as Resource.Success<List<Quote>>

            assertEquals(QuotesService.TYPE_TAG, fakeQuotesService.usedType)
            assertEquals(query, fakeQuotesService.usedFilter)
            assertEquals(data.size, result.data.size)
        }
    }
}