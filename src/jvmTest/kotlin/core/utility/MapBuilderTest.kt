package core.utility

import kotlin.test.Test
import kotlin.test.assertEquals


class MapBuilderTest {
    @Test
    fun buildBasic() {
        val expected = mapOf("Health" to "3", "Strength" to "1", "Bare Handed" to "2")

        val actual = map {
            entry("Health", 3)
            entry("Strength", 1)
            entry("Bare Handed", 2)
        }

        assertEquals(expected, actual)
    }

    @Test
    fun buildEmptyBases() {
        val expected = mapOf("Health" to "3", "Strength" to "1", "Bare Handed" to "2")

        val actual = mapUnbuilt {
            entry("Health", 3)
            entry("Strength", 1)
            entry("Bare Handed", 2)
        }.build(listOf())

        assertEquals(expected, actual)
    }
}