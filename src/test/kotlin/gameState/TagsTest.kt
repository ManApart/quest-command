package gameState

import core.properties.Tags
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TagsTest {

    @Test
    fun hasTag() {
        val tag = Tags(listOf("Apple"))
        assertTrue(tag.has("Apple"))
    }

    @Test
    fun doesNotHaveTag() {
        val tag = Tags(listOf("Apple"))
        assertFalse(tag.has("Pear"))
    }

    @Test
    fun hasTagRegardlessOfCapitalization() {
        val tag = Tags(listOf("Apple"))
        assertTrue(tag.has("aPpLE"))
    }

    @Test
    fun hasAll() {
        val tag = Tags(listOf("Apple", "Pear", "Orange"))
        val desired = Tags(listOf("Apple", "Pear"))
        assertTrue(tag.hasAll(desired))
    }

    @Test
    fun doesNotHaveAll() {
        val tag = Tags(listOf("Apple", "Pear"))
        val desired = Tags(listOf("Apple", "Pear", "Orange"))
        assertFalse(tag.hasAll(desired))
    }

    @Test
    fun hasNone() {
        val tag = Tags(listOf("Apple", "Pear"))
        val desired = Tags(listOf("Orange", "Banana"))
        assertTrue(tag.hasNone(desired))
    }

    @Test
    fun doesNotHaveNone() {
        val tag = Tags(listOf("Apple", "Pear"))
        val desired = Tags(listOf("Orange", "Pear"))
        assertFalse(tag.hasNone(desired))
    }

    @Test
    fun matches() {
        val tagA = Tags(listOf("Apple", "Pear"))
        val tagB = Tags(listOf("Pear", "ApPLe"))
        assertTrue(tagA.matches(tagB))
        assertTrue(tagB.matches(tagA))
    }

    @Test
    fun noMatchIfALarger() {
        val tagA = Tags(listOf("Apple", "Pear", "Orange"))
        val tagB = Tags(listOf("Pear", "ApPLe"))
        assertFalse(tagA.matches(tagB))
        assertFalse(tagB.matches(tagA))
    }

    @Test
    fun noMatchIfBLarger() {
        val tagA = Tags(listOf("Pear", "ApPLe"))
        val tagB = Tags(listOf("Apple", "Pear", "Orange"))
        assertFalse(tagA.matches(tagB))
        assertFalse(tagB.matches(tagA))
    }

    @Test
    fun doNotAddDuplicateTags() {
        val tag = Tags(listOf("Apple"))
        tag.add(("aPPlE"))
        assertEquals(1, tag.getAll().size)
        assertEquals("Apple", tag.getAll()[0])
    }

    @Test
    fun setFrom() {
        val tag = Tags(listOf("Apple"))
        val other = Tags(listOf("Pear"))
        tag.addAll(other)
        assertEquals(2, tag.getAll().size)
        assertEquals("Apple", tag.getAll()[0])
        assertEquals("Pear", tag.getAll()[1])
    }


}