package gameState

import core.properties.Values
import org.junit.Test
import kotlin.test.assertEquals

class ValuesTest {

    @Test
    fun setFrom() {
        val values = Values("Apple" to "1")
        val other = Values("Pear" to "2")
        values.setFrom(other)

        assertEquals(2, values.getAll().size)
        assertEquals(1, values.getInt("Apple"))
        assertEquals(2, values.getInt("Pear"))
    }

    @Test
    fun setFromOverrides() {
        val values = Values("Apple" to "1")
        val other = Values("Apple" to "2")
        values.setFrom(other)

        assertEquals(1, values.getAll().size)
        assertEquals(2, values.getInt("Apple"))
    }

    @Test
    fun incPositive() {
        val values = Values("Apple" to "1")
        values.inc("Apple", 3)

        assertEquals(1, values.getAll().size)
        assertEquals(4, values.getInt("Apple"))
    }

    @Test
    fun incNegative() {
        val values = Values("Apple" to "3")
        values.inc("APple", -1)

        assertEquals(1, values.getAll().size)
        assertEquals(2, values.getInt("Apple"))
    }

    @Test
    fun intOfWrongTypeReturnsDefault() {
        val values = Values("Apple" to "string")

        assertEquals(0, values.getInt("Apple"))
        assertEquals(2, values.getInt("Apple", 2))
    }

}