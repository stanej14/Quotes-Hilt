package cz.stanej14.quotes.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cz.stanej14.quotes.TestData
import cz.stanej14.quotes.ui.main.NavigationViewModel.NavigationEvent.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var underTest: NavigationViewModel

    @Before
    fun setUp() {
        underTest = NavigationViewModel()
    }

    @Test
    fun `Should navigate to feed after landing enter is clicked`() {
        assertFalse(underTest.navigationEvents.value?.peekContent() is Feed)

        underTest.navigateInside()

        assertTrue(underTest.navigationEvents.value?.peekContent() is Feed)
    }

    @Test
    fun `Should navigate to login after login is clicked`() {
        assertFalse(underTest.navigationEvents.value?.peekContent() is Login)

        underTest.navigateToLogin()

        assertTrue(underTest.navigationEvents.value?.peekContent() is Login)
    }

    @Test
    fun `Should navigate to quote detail`() {
        assertFalse(underTest.navigationEvents.value?.peekContent() is QuoteDetail)
        val quote = TestData.quote

        underTest.navigateToQuoteDetail(quote)

        val peekContent = underTest.navigationEvents.value?.peekContent()
        assertTrue(peekContent is QuoteDetail)
        assertEquals(quote, (peekContent as QuoteDetail).quote)
    }
}