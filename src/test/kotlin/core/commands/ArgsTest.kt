package core.commands

import combat.battle.position.TargetDirection
import org.junit.Test
import kotlin.test.assertEquals

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
}