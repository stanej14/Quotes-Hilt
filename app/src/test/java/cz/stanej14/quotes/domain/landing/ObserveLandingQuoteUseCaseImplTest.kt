package cz.stanej14.quotes.domain.landing

import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.common.CoroutinesMainDispatcherRule
import cz.stanej14.quotes.fake.data.FakeQuotesRepository
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ObserveLandingQuoteUseCaseImplTest {

    private lateinit var underTest: ObserveLandingQuoteUseCaseImpl
    private lateinit var fakeQuoteRepository: FakeQuotesRepository

    @Before
    fun setUp() {
        fakeQuoteRepository = FakeQuotesRepository()
        underTest = ObserveLandingQuoteUseCaseImpl(fakeQuoteRepository)
    }

    @Test
    fun `Should return quote of the day properly`() {
        val data = TestData.quote
        fakeQuoteRepository.quoteOfTheDayChannel.offer(data)
        runBlockingTest {
            val result = underTest.observeLandingQuote().first() as Resource.Success<Quote>

            assertEquals(data.tags, result.data.tags)
            assertEquals(data.author, result.data.author)
            assertEquals(data.body, result.data.body)
        }
    }
}