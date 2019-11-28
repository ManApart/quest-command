package core.commands

import org.junit.Test
import kotlin.test.assertEquals

class ResponseRequestTest {


    @Test
    fun assureYesVariant() {
        val input = mapOf("yes" to "correct")
        val response = ResponseRequest(input)

        assertEquals("correct", response.getCommand("y"))
    }

    @Test
    fun assureYesVariantB() {
        val input = mapOf("y" to "correct")
        val response = ResponseRequest(input)

        assertEquals("correct", response.getCommand("yes"))
    }

    @Test
    fun assureNoVariant() {
        val input = mapOf("no" to "correct")
        val response = ResponseRequest(input)

        assertEquals("correct", response.getCommand("n"))
    }

    @Test
    fun assureNoVariantB() {
        val input = mapOf("n" to "correct")
        val response = ResponseRequest(input)

        assertEquals("correct", response.getCommand("no"))
    }

    @Test
    fun getNthOption() {
        val input = mapOf("hot" to "false", "cold" to "false", "warm" to "just right")
        val response = ResponseRequest(input)

        assertEquals("just right", response.getCommand("  3  "))
    }

    @Test
    fun preferExactMatchToNthNumber() {
        val input = mapOf("1" to "do 1", "3" to "do 3", "5" to "do 5")
        val response = ResponseRequest(input)

        assertEquals("do 1", response.getCommand("1"))
        assertEquals("do 3", response.getCommand("3"))
    }

    @Test
    fun numberSymbolMatchesAnyNumber() {
        val input = mapOf("1" to "do 1", "3" to "do 3", "5" to "do 5", "#" to "good #")
        val response = ResponseRequest(input)

        assertEquals("good 2", response.getCommand("2"), "# takes precedence over index")
        assertEquals("do 3", response.getCommand("3"), "exact match takes precedence over #")
        assertEquals("good 4", response.getCommand("4"), "# takes precedence over index")
        assertEquals("good 20", response.getCommand("20"), "any number replaces #")
    }

    @Test
    fun getExactName() {
        val input = mapOf("hot" to "false", "cold" to "false", "warm" to "just right")
        val response = ResponseRequest(input)

        assertEquals("false", response.getCommand("  hot  "))
    }

    @Test
    fun ignorePartialMatch() {
        val input = mapOf("hot" to "false", "cold" to "false", "warm" to "just right")
        val response = ResponseRequest(input)

        assertEquals(null, response.getCommand("  c  "))
    }

}