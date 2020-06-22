package cz.stanej14.quotes.domain.detail

import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.fake.data.FakeQuotesRepository
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ObserveQuoteUseCaseImplTest {

    private lateinit var underTest: ObserveQuoteUseCaseImpl
    private lateinit var fakeQuoteRepository: FakeQuotesRepository

    @Before
    fun setUp() {
        fakeQuoteRepository = FakeQuotesRepository()
        underTest = ObserveQuoteUseCaseImpl(fakeQuoteRepository)
    }

    @Test
    fun `Should return quote of the day properly`() {
        val data = TestData.quote
        fakeQuoteRepository.quoteByIdChannel.offer(data)
        runBlockingTest {
            val result = underTest.observeQuote(data.id).first() as Resource.Success<Quote>

            assertEquals(data.tags, result.data.tags)
            assertEquals(data.author, result.data.author)
            assertEquals(data.body, result.data.body)
        }
    }
}