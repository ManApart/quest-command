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
    fun commaDelimiter() {
        val input = "cook apple, pear on knife".split(" ")
        val args = Args(input, delimiters = listOf(",", "on"))

        assertEquals("cook apple", args.getBaseString())
        assertEquals("pear", args.getString(","))
        assertEquals("knife", args.getString("on"))
    }

    @Test
    fun commaDelimiterWithSpace() {
        val input = "a , b, c".split(" ")
        val args = Args(input, delimiters = listOf(","))
        val delimited = args.getBaseAndStrings(",")

        assertEquals("a , b, c", args.fullString)
        assertEquals("a", args.getBaseString())
        assertEquals("a", delimited[0])
        assertEquals("b", delimited[1])
        assertEquals("c", delimited[2])
    }

    @Test
    fun commaDelimiterWithNoSpaces() {
        val input = "a,b,c".split(" ")
        val args = Args(input, listOf(ArgDelimiter(listOf(",", " "))))
        val delimited = args.getBaseAndStrings(",")
        val spaceDelimited = args.getBaseAndStrings(" ")

        assertEquals("a,b,c", args.fullString)
        assertEquals("a", args.getBaseString())

        assertEquals("a", delimited[0])
        assertEquals("b", delimited[1])
        assertEquals("c", delimited[2])

        assertEquals("a", spaceDelimited[0])
        assertEquals("b", spaceDelimited[1])
        assertEquals("c", spaceDelimited[2])
    }

    @Test
    fun spaceDelimiter() {
        val input = "a  b c".split(" ")
        val args = Args(input, listOf(ArgDelimiter(listOf(",", " "))))
        val delimited = args.getBaseAndStrings(",")
        val spaceDelimited = args.getBaseAndStrings(" ")

        assertEquals("a  b c", args.fullString)
        assertEquals("a", args.getBaseString())

        assertEquals("a", delimited[0])
        assertEquals("b", delimited[1])
        assertEquals("c", delimited[2])

        assertEquals("a", spaceDelimited[0])
        assertEquals("b", spaceDelimited[1])
        assertEquals("c", spaceDelimited[2])
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

    @Test
    fun getNumber() {
        val input = "1, two, 3".split(" ")
        val args = Args(input, delimiters = listOf(","))
        val numbers = args.getNumbers(",")
        val baseNumbers = args.getNumbers(",", true)

        assertEquals("1", args.getBaseString())
        assertEquals(1, args.getNumber())
        assertEquals(3, args.getNumber(","))
        assertEquals(null, numbers[0])
        assertEquals(3, numbers[1])

        assertEquals(1, args.getNumber(",", true))
        assertEquals(1, baseNumbers[0])
        assertEquals(null, baseNumbers[1])
        assertEquals(3, baseNumbers[2])
    }

    @Test
    fun repeatedWordIsPreserved() {
        val input = "rock 1 size 1 on rat".split(" ")
        val args = Args(input, delimiters = listOf("on"))

        assertEquals("rock 1 size 1", args.getBaseString())
        assertEquals("rat", args.getString("on"))

    }

    @Test
    fun excludedWordIsRemoved() {
        val input = "rock 1 size of rat".split(" ")
        val args = Args(input, excludedWords = listOf("SizE"))

        assertEquals("rock 1 of rat", args.getBaseString())
    }

    @Test
    fun getFirstString() {
        val input = "place in target".split(" ")
        val args = Args(input, delimiters = listOf("at", "in"))

        assertEquals("target", args.getFirstString("at", "in"))
        assertEquals("target", args.getFirstString("in", "at"))
    }

    @Test
    fun getFirstStringWhenBothPresent() {
        val input = "drop at container in space".split(" ")
        val args = Args(input, delimiters = listOf("at", "in"))

        assertEquals("container", args.getFirstString("at", "in"))
        assertEquals("space", args.getFirstString("in", "at"))
    }

}