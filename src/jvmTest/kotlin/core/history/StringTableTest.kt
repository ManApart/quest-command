package core.history

import org.junit.Test
import kotlin.test.assertTrue

class StringTableTest {

    @Test
    fun unevenGridDoesNotError() {
        val input = mutableListOf(
                listOf("Name", "Distance"),
                listOf("Name", "Distance", "Direction Path")
        )
        val table = StringTable(input)
        assertTrue(table.getString().isNotBlank())
    }

    @Test
    fun emptyGridGivesEmptyString() {
        val input = mutableListOf(listOf<String>())
        val table = StringTable(input)
        assertTrue(table.getString().isBlank())
    }
}