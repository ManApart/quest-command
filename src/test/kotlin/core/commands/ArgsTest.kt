package core.commands

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArgsTest {

    @Test
    fun groupsByIgnoredWordsAndDelimiters() {
        val input = "bottom center of rat".split(" ")
        val ignoredWords = listOf("bottom", "center", "top")
        val delimiters = listOf("with", "of")

        val args = Args(input, delimiters, ignoredWords)

        assertEquals(3, args.argGroups.size)
        assertEquals(3, args.argStrings.size)
        assertEquals("", args.getDelimited("base"))
        assertEquals("rat", args.getDelimited("of"))
        assertEquals("", args.getDelimited("with"))
    }

    @Test
    fun flagAtEnd() {
        val input = "t apple tree s".split(" ")
        val args = Args(input, flags = listOf("s"))

        assertEquals(1, args.argGroups.size)
        assertEquals(1, args.argStrings.size)
        assertEquals("t apple tree", args.argStrings[0])
        assertEquals("t apple tree s", args.fullString)
        assertFalse(args.hasFlag("t"))
        assertTrue(args.hasFlag("S"))
    }

    @Test
    fun flagInMiddle() {
        val input = "t apple a tree".split(" ")
        val args = Args(input, flags = listOf("a"))

        assertEquals(1, args.argGroups.size)
        assertEquals(1, args.argStrings.size)
        assertEquals("t apple tree", args.argStrings[0])
        assertEquals("t apple a tree", args.fullString)
        assertFalse(args.hasFlag("apple"))
        assertTrue(args.hasFlag("a"))
    }

    @Test
    fun flagWithDash() {
        val input = "t apple tree -f".split(" ")
        val args = Args(input, flags = listOf("f"))

        assertEquals(1, args.argGroups.size)
        assertEquals(1, args.argStrings.size)
        assertEquals("t apple tree", args.argStrings[0])
        assertEquals("t apple tree -f", args.fullString)
        assertFalse(args.hasFlag("s"))
        assertTrue(args.hasFlag("f"))
        assertTrue(args.hasFlag("-f"))
    }

    @Test
    fun delimiterMapWithTwoEmptyDelimiters() {
        val input = "cast heal 5 on for none".split(" ")
        val args = Args(input, delimiters = listOf("on", "for", "none"))

        assertEquals(4, args.argGroups.size)
        assertEquals(4, args.argStrings.size)
        assertEquals("cast heal 5 on for none", args.fullString)
        assertEquals("cast heal 5", args.argStrings[0])
        assertEquals("", args.argStrings[1])
        assertEquals("", args.argStrings[2])
        assertEquals("", args.getDelimited("on"))
        assertEquals("", args.getDelimited("for"))
        assertEquals("", args.getDelimited("none"))
    }

    @Test
    fun delimiterMap() {
        val input = "cast heal 5 on self for 4".split(" ")
        val args = Args(input, delimiters = listOf("on", "for", "none"))

        assertEquals("cast heal 5 on self for 4", args.fullString)
        assertEquals("cast heal 5", args.argStrings[0])
        assertEquals("self", args.getDelimited("on"))
        assertEquals("4", args.getDelimited("for"))
        assertEquals("", args.getDelimited("none"))
//        assertEquals("self", args.getDelimited("on"))
//        assertEquals("4", args.getDelimited("for"))
//        assertEquals("", args.getDelimited("none"))
    }

}