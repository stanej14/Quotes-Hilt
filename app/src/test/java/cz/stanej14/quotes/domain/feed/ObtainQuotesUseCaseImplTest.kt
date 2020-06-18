package cz.stanej14.quotes.domain.feed

import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.data.FakeQuotesService
import cz.stanej14.quotes.data.QuotesService.Companion.TYPE_TAG
import cz.stanej14.quotes.domain.network.mapper.QuoteMapper
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ObtainQuotesUseCaseImplTest {

    private lateinit var underTest: ObtainQuotesUseCaseImpl
    private lateinit var fakeQuotesService: FakeQuotesService
    private val mapper = QuoteMapper()

    @Before
    fun setUp() {
        fakeQuotesService = FakeQuotesService()
        underTest = ObtainQuotesUseCaseImpl(fakeQuotesService, mapper)
    }

    @Test
    fun `Should fail gracefully`() {
        fakeQuotesService.throwable = TestData.serverError
        runBlockingTest {
            val result = underTest.obtainQuotes()
            assertNull(fakeQuotesService.usedType)
            assertNull(fakeQuotesService.usedFilter)
            assertThat(result, instanceOf(Resource.Error::class.java))
        }
    }

    @Test
    fun `Should return quote of the day properly`() {
        val data = TestData.listOfQuoteDtos
        fakeQuotesService.quotes = data
        runBlockingTest {
            val result = underTest.obtainQuotes() as Resource.Success<List<Quote>>

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
            val result = underTest.obtainQuotes(query = query) as Resource.Success<List<Quote>>

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
            val result = underTest.obtainQuotes(
                query = query,
                shouldSearchByTag = true
            ) as Resource.Success<List<Quote>>

            assertEquals(TYPE_TAG, fakeQuotesService.usedType)
            assertEquals(query, fakeQuotesService.usedFilter)
            assertEquals(data.size, result.data.size)
        }
    }
}