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