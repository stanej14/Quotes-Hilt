package cz.stanej14.quotes.data

import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.domain.network.mapper.QuoteMapper
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
    fun `Should fail gracefully`() {
        fakeQuotesService.throwable = TestData.serverError
        runBlockingTest {
            val result = underTest.observeQuoteOfTheDay().first()

            assertNull(fakeQuotesService.usedType)
            assertNull(fakeQuotesService.usedFilter)
            assertThat(result, instanceOf(Resource.Error::class.java))
        }
    }
}