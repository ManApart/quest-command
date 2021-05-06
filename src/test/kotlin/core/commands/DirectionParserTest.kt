package core.commands

import traveling.direction.Direction
import traveling.position.Vector
import org.junit.Test
import kotlin.test.assertEquals

class DirectionParserTest {
    @Test
    fun vectorCommaSeparated() {
        val input = "1,2,3".split(" ")
        val result = parseVector(input)

        assertEquals(Vector(1, 2, 3), result)
    }

    @Test
    fun vectorSpaceSeparated() {
        val input = "4 3 2".split(" ")
        val result = parseVector(input)

        assertEquals(Vector(4, 3, 2), result)
    }

    @Test
    fun directionNames() {
        Direction.values().forEach {
            assertEquals(it, parseDirection(listOf(it.name)), "$it should parse correctly")
            assertEquals(it, parseDirection(listOf(it.name.lowercase())), "${it.name.lowercase()} should parse correctly")
        }
    }

    @Test
    fun directionShortcuts() {
        Direction.values().forEach {
            assertEquals(it, parseDirection(listOf(it.shortcut)), "${it.shortcut} should parse correctly")
            assertEquals(it, parseDirection(listOf(it.name.uppercase())), "${it.shortcut.uppercase()} should parse correctly")
        }
    }


}