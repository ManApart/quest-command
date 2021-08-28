package core.properties

import org.junit.Test
import kotlin.test.assertEquals

class PropsBuilderTest {

    @Test
    fun basicBuild() {
        val expected = Properties(
            Values(mapOf("one" to "1", "two" to "two")),
            Tags(listOf("Tag1", "Tag2"))
        )
        val actual = props {
            tag("Tag1", "Tag2")
            value("one", 1)
            value("two", "two")
        }
        assertEquals(expected, actual)
    }

}