package core.conditional

import org.junit.Test
import kotlin.test.assertEquals


class ConditionalStringBuilderTest{

    @Test
    fun buildWithInheritance(){
        val base = conditionalString {
            option("c")
            option("d")
        }

        val current = conditionalString {
            option("a")
            option("b")
        }

        val actual = current.build(listOf(base))

        assertEquals("a", actual.options[0].option)
        assertEquals("b", actual.options[1].option)
        assertEquals("c", actual.options[2].option)
        assertEquals("d", actual.options[3].option)

    }

    @Test
    fun buildWithInheritanceWhenNoDefaults(){
        val base = conditionalString {
            option("c")
            option("d")
        }

        val current = conditionalString()

        val actual = current.build(listOf(base))

        assertEquals("c", actual.options[0].option)
        assertEquals("d", actual.options[1].option)

    }
}