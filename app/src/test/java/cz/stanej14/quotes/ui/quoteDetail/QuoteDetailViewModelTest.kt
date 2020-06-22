package cz.stanej14.quotes.ui.quoteDetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.common.CoroutinesMainDispatcherRule
import cz.stanej14.quotes.domain.livedata.Event
import cz.stanej14.quotes.fake.FakeHasUserSessionUseCase
import cz.stanej14.quotes.fake.FakeObserveQuoteUseCase
import cz.stanej14.quotes.fake.domain.FakeToggleFavoriteQuoteUseCase
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import cz.stanej14.quotes.model.data
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuoteDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule(testDispatcher)

    private lateinit var fakeHasUserSessionUseCase: FakeHasUserSessionUseCase
    private lateinit var fakeObserveQuoteUseCase: FakeObserveQuoteUseCase
    private lateinit var underTest: QuoteDetailViewModel
    private lateinit var fakeToggleFavoriteQuoteUseCase: FakeToggleFavoriteQuoteUseCase

    @Before
    fun setUp() {

        fakeHasUserSessionUseCase = FakeHasUserSessionUseCase().apply {
            hasUserSession = true
        }
        fakeObserveQuoteUseCase = FakeObserveQuoteUseCase()
        fakeToggleFavoriteQuoteUseCase = FakeToggleFavoriteQuoteUseCase()

        underTest = QuoteDetailViewModel(
            fakeToggleFavoriteQuoteUseCase,
            fakeObserveQuoteUseCase,
            fakeHasUserSessionUseCase
        )
    }

    @Test
    fun `Should observe quote after initializing`() {
        val data = TestData.quote
        initializeViewModel(data)

        assertEquals(underTest.quote.value?.data, data)
    }

    @Test(expected = IllegalStateException::class)
    fun `Should throw an exception when trying to favorite a quote without proper initialization`() {
        underTest.onFavoriteClicked()
    }

    @Test
    fun `Should trigger login event when user tries to favorite a quote without being logged in`() {
        fakeHasUserSessionUseCase.hasUserSession = false
        val mockObserver = mockk<Observer<Event<Unit>>>(relaxed = true)
        underTest.loginEvent.observeForever(mockObserver)

        underTest.onFavoriteClicked()

        verify { mockObserver.onChanged(any()) }
    }

    @Test
    fun `Should favorite a quote properly`() {
        initializeViewModel()
        fakeToggleFavoriteQuoteUseCase.withSuccess()

        underTest.onFavoriteClicked()

        val result = underTest.favorite.value

        assertThat(result, instanceOf(Resource.Success::class.java))
    }

    @Test
    fun `Should handle favorite a quote error gracefully`() {
        initializeViewModel()
        fakeToggleFavoriteQuoteUseCase.withError()

        underTest.onFavoriteClicked()

        val result = underTest.favorite.value

        assertThat(result, instanceOf(Resource.Error::class.java))
    }

    private fun initializeViewModel(data: Quote = TestData.quote) {
        fakeObserveQuoteUseCase.withSuccess(data)

        underTest.initialize(data)
    }
}