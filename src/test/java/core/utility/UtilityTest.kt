package core.utility

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UtilityTest {

    @Test
    fun listsMatch() {
        val listA = listOf("wordA", "wordB", "WORDc")
        val listB = listOf("WORDb", "wordC", "wordA")

        assertTrue(listsMatch(listA, listB))
    }

    @Test
    fun listsWithDifferentSizeDoNotMatch() {
        val listA = listOf("wordB", "WORDc")
        val listB = listOf("WORDb", "wordC", "wordA")

        assertFalse(listsMatch(listA, listB))
    }

    @Test
    fun listsWithDifferentContentsDoNotMatch() {
        val listA = listOf("wordB", "WORDc", "wordG")
        val listB = listOf("WORDb", "wordC", "wordA")

        assertFalse(listsMatch(listA, listB))
    }

    @Test
    fun mapsMatch() {
        val mapA = mapOf("one" to 1, "two" to 2)
        val mapB = mapOf("one" to 1, "two" to 2)

        assertTrue(mapsMatch(mapA, mapB))
    }

    @Test
    fun mapsWithDifferentSizeDoNotMatch() {
        val mapA = mapOf("one" to 1, "two" to 2)
        val mapB = mapOf("one" to 1, "two" to 2, "three" to 3)

        assertFalse(mapsMatch(mapA, mapB))
    }

    @Test
    fun mapsWithDifferentContentsDoNotMatch() {
        val mapA = mapOf("one" to 1, "two" to 3)
        val mapB = mapOf("one" to 1, "two" to 2)

        assertFalse(mapsMatch(mapA, mapB))
    }
}