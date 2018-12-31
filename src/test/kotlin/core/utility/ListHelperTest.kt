package core.utility

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ListHelperTest {

    @Test
    fun listsMatch() {
        val listA = listOf("wordA", "wordB", "WORDc")
        val listB = listOf("WORDb", "wordC", "wordA")

        assertTrue(listA.matches(listB))
    }

    @Test
    fun listsWithDifferentSizeDoNotMatch() {
        val listA = listOf("wordB", "WORDc")
        val listB = listOf("WORDb", "wordC", "wordA")

        assertFalse(listA.matches(listB))
    }

    @Test
    fun listsWithDifferentContentsDoNotMatch() {
        val listA = listOf("wordB", "WORDc", "wordG")
        val listB = listOf("WORDb", "wordC", "wordA")

        assertFalse(listA.matches(listB))
    }

    @Test
    fun filterUniqueByName() {
        val item1 = FakeNamed("Item1")
        val item2 = FakeNamed("Item1")
        val item3 = FakeNamed("Item2")
        val list = listOf(item1, item2, item3)
        val results = list.filterUniqueByName()

        assertEquals(2, results.size)
        assertEquals(item1, results[0])
        assertEquals(item3, results[1])
    }
}
