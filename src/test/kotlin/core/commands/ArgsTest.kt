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
        val delimiters = listOf(ArgDelimiter("with"), ArgDelimiter("of"))
        val args = Args(input, delimiters, ignoredWords)

        assertEquals("", args.getString("base"))
        assertEquals("rat", args.getString("of"))
        assertEquals("", args.getString("with"))

        assertEquals(listOf(), args.getGroup("base"))
        assertEquals(listOf("rat"), args.getGroup("of"))
        assertEquals(listOf(), args.getGroup("with"))
    }

    @Test
    fun flagAtEnd() {
        val input = "t apple tree s".split(" ")
        val args = Args(input, flags = listOf("s"))

        assertEquals("t apple tree", args.getBaseString())
        assertEquals("t apple tree s", args.fullString)
        assertFalse(args.hasFlag("t"))
        assertTrue(args.hasFlag("S"))
    }

    @Test
    fun flagInMiddle() {
        val input = "t apple a tree".split(" ")
        val args = Args(input, flags = listOf("a"))

        assertEquals("t apple tree", args.getBaseString())
        assertEquals("t apple a tree", args.fullString)
        assertFalse(args.hasFlag("apple"))
        assertTrue(args.hasFlag("a"))
    }

    @Test
    fun flagWithDash() {
        val input = "t apple tree -f".split(" ")
        val args = Args(input, flags = listOf("f"))

        assertEquals("t apple tree", args.getBaseString())
        assertEquals("t apple tree -f", args.fullString)
        assertFalse(args.hasFlag("s"))
        assertTrue(args.hasFlag("f"))
        assertTrue(args.hasFlag("-f"))
    }

    @Test
    fun delimiterMapWithTwoEmptyDelimiters() {
        val input = "cast heal 5 on for none".split(" ")
        val args = Args(input, delimiters = listOf("on", "for", "none"))

        assertEquals("cast heal 5 on for none", args.fullString)
        assertEquals("cast heal 5", args.getBaseString())
        assertEquals("", args.getString("on"))
        assertEquals("", args.getString("for"))
        assertEquals("", args.getString("none"))
    }

    @Test
    fun delimiterMap() {
        val input = "cast heal 5 on self for 4".split(" ")
        val args = Args(input, delimiters = listOf("on", "for", "none"))

        assertEquals("cast heal 5 on self for 4", args.fullString)
        assertEquals("cast heal 5", args.getBaseString())
        assertEquals("self", args.getString("on"))
        assertEquals("4", args.getString("for"))
        assertEquals("", args.getString("none"))
    }

    @Test
    fun characterDelimiters() {
        val input = "cook apple, pear on knife".split(" ")
        val args = Args(input, delimiters = listOf(",", "on"))

        assertEquals("cook apple", args.getBaseString())
        assertEquals("pear", args.getString(","))
        assertEquals("knife", args.getString("on"))
    }

    @Test
    fun repeatedDelimiters() {
        val input = "cook apple and pear and banana on knife".split(" ")
        val args = Args(input, delimiters = listOf("and", "on"))

        assertEquals("cook apple", args.getBaseString())
        assertEquals(listOf(listOf("cook", "apple"), listOf("pear"), listOf("banana")), args.getBaseAndGroups("and"))
        assertEquals(listOf("cook apple", "pear", "banana"), args.getBaseAndStrings("and"))

        assertEquals("pear", args.getString("and"))
        assertEquals(listOf("pear", "banana"), args.getStrings("and"))
        assertEquals("knife", args.getString("on"))
    }

    @Test
    fun delimiterByAlias() {
        val input = "cook apple and pear on knife".split(" ")
        val args = Args(input, delimiters = listOf(ArgDelimiter(listOf(",", "and")), ArgDelimiter("on")))

        assertEquals("cook apple", args.getBaseString())

        assertEquals(true, args.hasGroup(","))
        assertEquals("pear", args.getString(","))

        assertEquals(true, args.hasGroup("and"))
        assertEquals("pear", args.getString("and"))

        assertEquals(true, args.hasGroup("on"))
        assertEquals("knife", args.getString("on"))
    }


}