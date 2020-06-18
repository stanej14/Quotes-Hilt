package cz.stanej14.quotes.ui.feed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.common.CoroutinesMainDispatcherRule
import cz.stanej14.quotes.fake.domain.FakeObtainQuotesUseCase
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FeedViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule(testDispatcher)

    private lateinit var underTest: FeedViewModel
    private lateinit var fakeObtainQuotesUseCase: FakeObtainQuotesUseCase

    @Before
    fun setUp() {
        fakeObtainQuotesUseCase = FakeObtainQuotesUseCase()
    }

    @Test
    fun `Should show error when obtaining quotes fails`() {
        fakeObtainQuotesUseCase.withError(TestData.serverError)
        underTest = FeedViewModel(fakeObtainQuotesUseCase)

        val result = underTest.quotes.value

        assertThat(result, instanceOf(Resource.Error::class.java))
    }

    @Test
    fun `Should show quotes successfully`() {
        fakeObtainQuotesUseCase.withSuccess(TestData.listOfQuotes)
        underTest = FeedViewModel(fakeObtainQuotesUseCase)

        val result = underTest.quotes.value

        assertThat(result, instanceOf(Resource.Success::class.java))
    }

    @Test
    fun `Should recover from error after refreshing`() {
        fakeObtainQuotesUseCase.withError(TestData.serverError)
        underTest = FeedViewModel(fakeObtainQuotesUseCase)
        val observer: Observer<Resource<List<Quote>>> = mockk(relaxed = true)
        underTest.quotes.observeForever(observer)

        var result = underTest.quotes.value
        assertThat(result, instanceOf(Resource.Error::class.java))

        fakeObtainQuotesUseCase.withSuccess(TestData.listOfQuotes)
        underTest.onRefresh()

        result = underTest.quotes.value
        assertThat(result, instanceOf(Resource.Success::class.java))

        verifyOrder {
            observer.onChanged(Resource.Error(TestData.serverError))
            observer.onChanged(Resource.Loading)
            observer.onChanged(Resource.Success(TestData.listOfQuotes))
        }
    }

    @Test
    fun `Should load data based on given query`() {
        fakeObtainQuotesUseCase.withSuccess(TestData.listOfQuotes)
        underTest = FeedViewModel(fakeObtainQuotesUseCase)
        val query = "my query"

        runBlockingTest {
            underTest.onQueryUpdated(query)
            testDispatcher.advanceUntilIdle()
        }

        val result = underTest.quotes.value
        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(query, fakeObtainQuotesUseCase.usedQuery)
    }

    @Test
    fun `Should load data based on given tag`() {
        fakeObtainQuotesUseCase.withSuccess(TestData.listOfQuotes)
        underTest = FeedViewModel(fakeObtainQuotesUseCase)
        val tag = "my tag"

        runBlockingTest {
            underTest.onTagClicked(tag)
            testDispatcher.advanceUntilIdle()
        }

        val result = underTest.quotes.value
        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(tag, fakeObtainQuotesUseCase.usedQuery)
        assertTrue(fakeObtainQuotesUseCase.usedSearchByTag!!)
    }

    @Test
    fun `Should treat empty query as null`() {
        fakeObtainQuotesUseCase.withSuccess(TestData.listOfQuotes)
        underTest = FeedViewModel(fakeObtainQuotesUseCase)

        runBlockingTest {
            underTest.onQueryUpdated("      ")
            testDispatcher.advanceUntilIdle()
        }

        val result = underTest.quotes.value
        assertThat(result, instanceOf(Resource.Success::class.java))
        assertNull(fakeObtainQuotesUseCase.usedQuery)
    }

    @Test
    fun `Should load data for the same query only once`() {
        fakeObtainQuotesUseCase.withSuccess(TestData.listOfQuotes)
        underTest = FeedViewModel(fakeObtainQuotesUseCase)
        fakeObtainQuotesUseCase.resetCount()

        runBlockingTest {
            underTest.onQueryUpdated("my query")
            testDispatcher.advanceUntilIdle()
            underTest.onQueryUpdated("my query")
            testDispatcher.advanceUntilIdle()
        }

        val result = underTest.quotes.value
        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(1, fakeObtainQuotesUseCase.calls)
    }

    @Test
    fun `Should debounce query`() {
        fakeObtainQuotesUseCase.withSuccess(TestData.listOfQuotes)
        underTest = FeedViewModel(fakeObtainQuotesUseCase)
        fakeObtainQuotesUseCase.resetCount()
        val firstQuery = "my query"
        val secondQuery = "my query second"

        runBlockingTest {
            underTest.onQueryUpdated(firstQuery)
            underTest.onQueryUpdated(secondQuery)
            testDispatcher.advanceUntilIdle()
        }

        val result = underTest.quotes.value
        assertThat(result, instanceOf(Resource.Success::class.java))
        assertEquals(1, fakeObtainQuotesUseCase.calls)
        assertEquals(secondQuery, fakeObtainQuotesUseCase.usedQuery)
    }
}