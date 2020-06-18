package cz.stanej14.quotes.domain.landing

import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.data.FakeQuotesService
import cz.stanej14.quotes.domain.network.error.NetworkError
import cz.stanej14.quotes.domain.network.mapper.QuoteMapper
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

@ExperimentalCoroutinesApi
class ObtainLandingQuoteUseCaseImplTest {

    private lateinit var underTest: ObtainLandingQuoteUseCaseImpl
    private lateinit var fakeQuotesService: FakeQuotesService
    private val mapper = QuoteMapper()

    @Before
    fun setUp() {
        fakeQuotesService = FakeQuotesService()
        underTest = ObtainLandingQuoteUseCaseImpl(fakeQuotesService, mapper)
    }

    @Test
    fun `Should fail gracefully`() {
        fakeQuotesService.throwable = TestData.serverError
        runBlockingTest {
            val result = underTest.obtainLandingQuote()
            assertThat(result, instanceOf(Resource.Error::class.java))
        }
    }

    @Test
    fun `Should return quote of the day properly`() {
        val data = TestData.quoteDto
        fakeQuotesService.quoteOfTheDay = data
        runBlockingTest {
            val result = underTest.obtainLandingQuote() as Resource.Success<Quote>

            assertEquals(data.tags, result.data.tags)
            assertEquals(data.author, result.data.author)
            assertEquals(data.body, result.data.body)
        }
    }
}