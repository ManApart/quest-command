package core.utility

import org.junit.Test
import kotlin.test.*

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

    @Test
    fun getRandom() {
        val list = listOf("Test 1", "Test 2")
        assertNotNull(list.random())
    }

    @Test
    fun getRandomFromEmptyList() {
        assertNull(listOf<String>().random())
    }

    @Test
    fun safeSubListPreventsStartBefore0() {
        val sublist = listOf(0,1,2,3).safeSubList(-1)
        assertEquals(listOf(0,1,2,3), sublist)
    }

    @Test
    fun safeSubListPreventsEndBefore0() {
        val sublist = listOf(0,1,2,3).safeSubList(0, -1)
        assertEquals(listOf(), sublist)
    }

    @Test
    fun safeSubListPreventsEndBeforeStart() {
        val sublist = listOf(0,1,2,3).safeSubList(2, 1)
        assertEquals(listOf(), sublist)
    }

    @Test
    fun safeSubListPreventsEndGreaterThanSize() {
        val sublist = listOf(0,1,2,3).safeSubList(0, 5)
        assertEquals(listOf(0,1,2,3), sublist)
    }

    @Test
    fun safeSubListPreventsStartGreaterThanSize() {
        val sublist = listOf(0,1,2,3).safeSubList(5, 6)
        assertEquals(listOf(), sublist)
    }

}
