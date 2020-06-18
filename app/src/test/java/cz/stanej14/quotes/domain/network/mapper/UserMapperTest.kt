package cz.stanej14.quotes.domain.network.mapper

import cz.stanej14.quotes.TestData
import org.junit.Assert.assertEquals
import org.junit.Test

class UserMapperTest {

    private val underTest = UserMapper()

    @Test
    fun `Should map properly`() {
        val data = TestData.sessionResponse

        val result = underTest.map(data)

        assertEquals(data.email, result.email)
        assertEquals(data.login, result.login)
    }

}