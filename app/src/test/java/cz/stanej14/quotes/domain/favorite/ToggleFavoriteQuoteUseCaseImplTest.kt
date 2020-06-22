package cz.stanej14.quotes.domain.favorite

import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.data.FakeQuotesService
import cz.stanej14.quotes.data.QuotesRepository
import cz.stanej14.quotes.domain.detail.ToggleFavoriteQuoteUseCaseImpl
import cz.stanej14.quotes.domain.network.mapper.QuoteMapper
import cz.stanej14.quotes.model.Resource
import cz.stanej14.quotes.model.data
import io.mockk.called
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ToggleFavoriteQuoteUseCaseImplTest {

    private lateinit var fakeQuotesService: FakeQuotesService
    private lateinit var mockQuotesRepository: QuotesRepository
    private lateinit var underTest: ToggleFavoriteQuoteUseCaseImpl
    private val quoteMapper = QuoteMapper()

    @Before
    fun setUp() {
        fakeQuotesService = FakeQuotesService()
        mockQuotesRepository = mockk(relaxed = true)
        underTest =
            ToggleFavoriteQuoteUseCaseImpl(
                mockQuotesRepository,
                fakeQuotesService,
                quoteMapper
            )
    }

    @Test
    fun `Should properly favor a quote`() {
        val data = TestData.quote.copy(isFavorite = false)
        val updatedData = TestData.quoteDto
        val mappedUpdatedData = quoteMapper.map(updatedData)!!
        fakeQuotesService.favoriteQuoteDto = updatedData

        runBlockingTest {
            val result = underTest.toggleFavorite(data)

            verify { mockQuotesRepository.onQuoteFavoriteChange(mappedUpdatedData) }
            assertThat(result, instanceOf(Resource.Success::class.java))
        }
    }

    @Test
    fun `Should fail gracefully while favoring a quote`() {
        val data = TestData.quote.copy(isFavorite = false)
        fakeQuotesService.throwable = TestData.serverError

        runBlockingTest {
            val result = underTest.toggleFavorite(data)

            verify { mockQuotesRepository wasNot called }
            assertThat(result, instanceOf(Resource.Error::class.java))
        }
    }

    @Test
    fun `Should properly unfavor a quote`() {
        val data = TestData.quote.copy(isFavorite = true)
        val updatedData = TestData.quoteDto
        val mappedUpdatedData = quoteMapper.map(updatedData)!!
        fakeQuotesService.unfavoriteQuoteDto = updatedData

        runBlockingTest {
            val result = underTest.toggleFavorite(data)

            verify { mockQuotesRepository.onQuoteFavoriteChange(mappedUpdatedData) }
            assertThat(result, instanceOf(Resource.Success::class.java))
        }
    }

    @Test
    fun `Should fail gracefully while unfavoring a quote`() {
        val data = TestData.quote.copy(isFavorite = true)
        fakeQuotesService.throwable = TestData.serverError

        runBlockingTest {
            val result = underTest.toggleFavorite(data)

            verify { mockQuotesRepository wasNot called }
            assertThat(result, instanceOf(Resource.Error::class.java))
        }
    }
}