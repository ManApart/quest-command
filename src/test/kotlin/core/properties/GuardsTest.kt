package core.properties

import org.junit.Test
import kotlin.test.assertEquals

class GuardsTest {

    @Test
    fun isGreaterThanZero() {
        assertEquals("50", isGreaterThanEqualToZero("100", "50"))
        assertEquals("0", isGreaterThanEqualToZero("100", "0"))
        assertEquals("0", isGreaterThanEqualToZero("100", "-50"))
    }

    @Test
    fun numberRange() {
        val rangeCheck = createNumberRange(0, 10)
        assertEquals("6", rangeCheck("5", "6"))
        assertEquals("0", rangeCheck("5", "0"))
        assertEquals("0", rangeCheck("5", "-50"))
        assertEquals("10", rangeCheck("5", "10"))
        assertEquals("10", rangeCheck("5", "11"))
    }

    @Test
    fun numberRangeWithDifferentRange() {
        val rangeCheck = createNumberRange(5, 100)
        assertEquals("6", rangeCheck("5", "6"))
        assertEquals("5", rangeCheck("5", "0"))
        assertEquals("5", rangeCheck("5", "-50"))
        assertEquals("10", rangeCheck("5", "10"))
        assertEquals("11", rangeCheck("5", "11"))
        assertEquals("100", rangeCheck("5", "100"))
        assertEquals("100", rangeCheck("5", "110"))
    }

}