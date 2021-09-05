package core.properties

import org.junit.Test
import kotlin.test.assertEquals

class PropsBuilderTest {

    @Test
    fun basicEquality() {
        val a = Properties(Values(mapOf("Thing" to "1", "Other" to "two")), Tags(listOf("Bob", "Jim")))
        val b = Properties(Values(mapOf("Other" to "two", "Thing" to "1")), Tags(listOf("Jim", "Bob")))
        assertEquals(a,b)
    }

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

    @Test
    fun extends() {
        val expected = Properties(
            Values(mapOf("one" to "1", "two" to "two", "three" to "four")),
            Tags(listOf("Tag", "Tagz"))
        )
        val actual = propsUnbuilt {
            tag("Tag")
            value("one", 1)
        }

        val bases = listOf(
            propsUnbuilt {
                tag("Tagz")
                value("two", "two")
                value("three", "three")
            },
            propsUnbuilt {
                value("three", "four")
            }
        )

        assertEquals(expected, actual.build(bases))
    }

}