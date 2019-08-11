package core.utility

import org.junit.Test
import kotlin.test.assertEquals

class StringHelperTest {

    @Test
    fun caseInsensitive() {
        val base = "\$fireHealth"
        val params = mapOf("fireHealth" to "1")

        assertEquals("1", base.apply(params))
    }

    @Test
    fun onlyEscapedVariablesReplaced() {
        val base = "\$fireHealth fireHealth"
        val params = mapOf("fireHealth" to "1")

        assertEquals("1 fireHealth", base.apply(params))
    }

    @Test
    fun variablesThatHaveTheSameStartAreProperlyReplaced() {
        val base = "\$health \$healthCurrent"
        val params = mapOf("health" to "2", "healthCurrent" to "1")

        assertEquals("2 1", base.apply(params))
    }

    @Test
    fun variableInMiddleOfStringIsReplaced() {
        val base = "My stat is \$healthCurrent, health"
        val params = mapOf("health" to "2", "healthCurrent" to "1")

        assertEquals("My stat is 1, health", base.apply(params))
    }

    @Test
    fun onlyTheCorrectVariableIsReplaced() {
        val base = "\$sourceItem"
        val params = mapOf("sourceItem" to "Wheat Bundle", "resultItem" to "Wheat Flour", "resultItemLocation" to "Windmill")

        assertEquals("Wheat Bundle", base.apply(params))
    }

    @Test
    fun repeat0TimesIsBlank() {
        assertEquals("", "test".repeat(0))
    }
    @Test
    fun repeat() {
        assertEquals("testtest", "test".repeat(2))
    }

}