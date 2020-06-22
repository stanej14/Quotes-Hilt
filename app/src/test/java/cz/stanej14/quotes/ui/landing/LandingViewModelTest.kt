package cz.stanej14.quotes.ui.landing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.common.CoroutinesMainDispatcherRule
import cz.stanej14.quotes.fake.domain.FakeObserveLandingQuoteUseCase
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LandingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule(TestCoroutineDispatcher())

    private lateinit var underTest: LandingViewModel
    private lateinit var fakeObtainLandingQuoteUseCase: FakeObserveLandingQuoteUseCase

    @Before
    fun setUp() {
        fakeObtainLandingQuoteUseCase = FakeObserveLandingQuoteUseCase()
    }

    @Test
    fun `Should show error when obtaining quote fails`() {
        fakeObtainLandingQuoteUseCase.withError(TestData.serverError)
        underTest = LandingViewModel(fakeObtainLandingQuoteUseCase)

        val result = underTest.quote.value

        assertThat(result, instanceOf(Resource.Error::class.java))
    }

    @Test
    fun `Should show quote successfully`() {
        fakeObtainLandingQuoteUseCase.withSuccess(TestData.quote)
        underTest = LandingViewModel(fakeObtainLandingQuoteUseCase)

        val result = underTest.quote.value

        assertThat(result, instanceOf(Resource.Success::class.java))
    }

    @Test
    fun `Should recover from error after refreshing`() {
        fakeObtainLandingQuoteUseCase.withError(TestData.serverError)
        underTest = LandingViewModel(fakeObtainLandingQuoteUseCase)
        val observer: Observer<Resource<Quote>> = mockk(relaxed = true)
        underTest.quote.observeForever(observer)

        var result = underTest.quote.value
        assertThat(result, instanceOf(Resource.Error::class.java))

        fakeObtainLandingQuoteUseCase.withSuccess(TestData.quote)
        underTest.onRefresh()

        result = underTest.quote.value
        assertThat(result, instanceOf(Resource.Success::class.java))

        verifyOrder {
            observer.onChanged(Resource.Error(TestData.serverError))
            observer.onChanged(Resource.Loading)
            observer.onChanged(Resource.Success(TestData.quote))
        }
    }
}