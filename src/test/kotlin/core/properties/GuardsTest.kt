package core.properties

import org.junit.Test
import status.stat.AIR_MAGIC
import status.stat.HEALTH
import status.stat.WATER_MAGIC
import traveling.location.location.HEAT
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

    @Test
    fun acceptedValuesList() {
        val valuesCheck = createAcceptedValuesList(listOf("yes", "no"))
        assertEquals("initial", valuesCheck("initial", "NotValid"))
        assertEquals("yes", valuesCheck("initial", "yes"))
        assertEquals("no", valuesCheck("initial", "no"))
        assertEquals("initial", valuesCheck("initial", "NO"))
    }

    @Test
    fun acceptedValuesWithDifferentList() {
        val valuesCheck = createAcceptedValuesList(listOf(HEALTH, HEAT, AIR_MAGIC))
        assertEquals(HEALTH, valuesCheck("magic", HEALTH))
        assertEquals(HEAT, valuesCheck("fire", HEAT))
        assertEquals(AIR_MAGIC, valuesCheck("magic", AIR_MAGIC))
        assertEquals("fire", valuesCheck("fire", WATER_MAGIC))
    }

}