package core.utility

import org.junit.Test
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
}