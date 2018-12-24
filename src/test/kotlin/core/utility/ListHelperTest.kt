package core.utility

import org.junit.Test
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
}