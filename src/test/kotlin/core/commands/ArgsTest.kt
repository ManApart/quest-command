package core.commands

import combat.battle.position.TargetDirection
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArgsTest {

    @Test
    fun groupsByIgnoredWordsAndDelimiters() {
        val input = "bottom center of rat".split(" ")
        val ignoredWords = TargetDirection.getAllAliases()
        val delimiters = listOf("with", "of")

        val args = Args(input, delimiters, ignoredWords)

        assertEquals(2, args.argGroups.size)
        assertEquals(2, args.argStrings.size)
        assertEquals("", args.argStrings[0])
        assertEquals("rat", args.argStrings[1])
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

}