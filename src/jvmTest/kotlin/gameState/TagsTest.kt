package gameState

import core.properties.Tags
import kotlin.test.Test

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TagsTest {

    @Test
    fun hasTag() {
        val tag = Tags("Apple")
        assertTrue(tag.has("Apple"))
    }

    @Test
    fun doesNotHaveTag() {
        val tag = Tags("Apple")
        assertFalse(tag.has("Pear"))
    }

    @Test
    fun hasTagRegardlessOfCapitalization() {
        val tag = Tags("Apple")
        assertTrue(tag.has("aPpLE"))
    }

    @Test
    fun hasAll() {
        val tag = Tags("Apple", "Pear", "Orange")
        val desired = Tags("Apple", "Pear")
        assertTrue(tag.hasAll(desired))
    }

    @Test
    fun doesNotHaveAll() {
        val tag = Tags("Apple", "Pear")
        val desired = Tags("Apple", "Pear", "Orange")
        assertFalse(tag.hasAll(desired))
    }

    @Test
    fun hasNone() {
        val tag = Tags("Apple", "Pear")
        val desired = Tags("Orange", "Banana")
        assertTrue(tag.hasNone(desired))
    }

    @Test
    fun doesNotHaveNone() {
        val tag = Tags("Apple", "Pear")
        val desired = Tags("Orange", "Pear")
        assertFalse(tag.hasNone(desired))
    }

    @Test
    fun matches() {
        val tagA = Tags("Apple", "Pear")
        val tagB = Tags("Pear", "ApPLe")
        assertTrue(tagA.matches(tagB))
        assertTrue(tagB.matches(tagA))
    }

    @Test
    fun noMatchIfALarger() {
        val tagA = Tags("Apple", "Pear", "Orange")
        val tagB = Tags("Pear", "ApPLe")
        assertFalse(tagA.matches(tagB))
        assertFalse(tagB.matches(tagA))
    }

    @Test
    fun noMatchIfBLarger() {
        val tagA = Tags("Pear", "ApPLe")
        val tagB = Tags("Apple", "Pear", "Orange")
        assertFalse(tagA.matches(tagB))
        assertFalse(tagB.matches(tagA))
    }

    @Test
    fun doNotAddDuplicateTags() {
        val tag = Tags("Apple")
        tag.add(("aPPlE"))
        assertEquals(1, tag.getAll().size)
        assertEquals("Apple", tag.getAll()[0])
    }

    @Test
    fun setFrom() {
        val tag = Tags("Apple")
        val other = Tags("Pear")
        tag.addAll(other)
        assertEquals(2, tag.getAll().size)
        assertEquals("Apple", tag.getAll()[0])
        assertEquals("Pear", tag.getAll()[1])
    }


}