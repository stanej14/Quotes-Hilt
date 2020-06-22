package cz.stanej14.quotes.domain.feed

import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.fake.data.FakeQuotesRepository
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ObserveQuotesUseCaseImplTest {

    private lateinit var underTest: ObserveQuotesUseCaseImpl
    private lateinit var fakeQuotesRepository: FakeQuotesRepository

    @Before
    fun setUp() {
        fakeQuotesRepository = FakeQuotesRepository()
        underTest = ObserveQuotesUseCaseImpl(fakeQuotesRepository)
    }

    @Test
    fun `Should return quote of the day properly`() {
        val data = TestData.listOfQuotes
        fakeQuotesRepository.quotesChannel.offer(data)
        runBlockingTest {
            val result = underTest.observeQuotes().first() as Resource.Success<List<Quote>>

            assertThat(result, instanceOf(Resource.Success::class.java))
        }
    }
}