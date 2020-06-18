package cz.stanej14.quotes.domain.network.mapper

import cz.stanej14.quotes.TestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class QuoteMapperTest {

    private val underTest = QuoteMapper()

    @Test
    fun `Should map quote properly`() {
        val data = TestData.quoteDto

        val result = underTest.map(data)!!

        assertEquals(data.id, result.id)
        assertEquals(data.author, result.author)
        assertEquals(data.body, result.body)
        assertEquals(data.tags, result.tags)
        assertEquals(data.userDetails?.favorite, result.isFavorite)
    }

    @Test
    fun `Should map null favorite to false`() {
        val data = TestData.quoteDto.copy(userDetails = null)

        val result = underTest.map(data)!!

        assertEquals(data.id, result.id)
        assertEquals(data.author, result.author)
        assertEquals(data.body, result.body)
        assertEquals(data.tags, result.tags)
        assertEquals(false, result.isFavorite)
    }

    @Test
    fun `Empty author means empty quote`() {
        val data = TestData.quoteDto.copy(author = null)

        assertNull(underTest.map(data))
    }
}