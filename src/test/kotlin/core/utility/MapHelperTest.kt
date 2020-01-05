package core.utility

import org.junit.Test
import traveling.location.location.LocationTarget
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MapHelperTest {
    @Test
    fun mapsMatch() {
        val mapA = mapOf("one" to 1, "two" to 2)
        val mapB = mapOf("one" to 1, "two" to 2)

        assertTrue(mapA.matches(mapB))
    }

    @Test
    fun mapsWithDifferentSizeDoNotMatch() {
        val mapA = mapOf("one" to 1, "two" to 2)
        val mapB = mapOf("one" to 1, "two" to 2, "three" to 3)

        assertFalse(mapA.matches(mapB))
    }

    @Test
    fun mapsWithDifferentContentsDoNotMatch() {
        val mapA = mapOf("one" to 1, "two" to 3)
        val mapB = mapOf("one" to 1, "two" to 2)

        assertFalse(mapA.matches(mapB))
    }

    @Test
    fun mapAHasAllOfMapB() {
        val mapA = mapOf("one" to 1, "two" to 2, "three" to 3)
        val mapB = mapOf("one" to 1, "two" to 2)

        assertTrue(mapA.hasAllOf(mapB))
    }

    @Test
    fun mapADoesNotHaveAllOfMapB() {
        val mapA = mapOf("one" to 1, "two" to 2)
        val mapB = mapOf("one" to 1, "two" to 2, "three" to 3)

        assertFalse(mapA.hasAllOf(mapB))
    }

    @Test
    fun mapADoesNotHaveSameValuesAsMapB() {
        val mapA = mapOf("one" to 1, "two" to 2)
        val mapB = mapOf("one" to 1, "two" to 3)

        assertFalse(mapA.hasAllOf(mapB))
    }

    @Test
    fun applyParams() {
        val mapA = mapOf("item" to "\$sourceItem")
        val mapB = mapOf("sourceItem" to "Wheat Bundle", "resultItem" to "Wheat Flour", "resultItemLocation" to "Windmill")
        val result = mapA.apply(mapB)

        assertEquals(1, result.keys.size)
        assertEquals("Wheat Bundle", result["item"])
    }

    @Test
    fun getAllStrings() {
        val map = mapOf("One" to "1", "Two" to mapOf("Three" to "three"), "Four" to LocationTarget("IgnoredValue"), LocationTarget("IgnoredKey") to "IgnoredValue2")
        val expected = listOf("One", "1", "Two", "Three", "three", "Four")
        assertEquals(expected, map.getAllStrings())
    }

}