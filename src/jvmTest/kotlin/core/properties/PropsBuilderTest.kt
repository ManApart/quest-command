package core.properties

import kotlin.test.Test
import kotlin.test.assertEquals

class PropsBuilderTest {

    @Test
    fun basicEquality() {
        val a = Properties(Values("Thing" to "1", "Other" to "two"), Tags("Bob", "Jim"))
        val b = Properties(Values("Other" to "two", "Thing" to "1"), Tags("Jim", "Bob"))
        assertEquals(a,b)
    }

    @Test
    fun basicBuild() {
        val expected = Properties(
            Values("one" to "1", "two" to "two"),
            Tags("Tag1", "Tag2")
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
            Values("one" to "1", "two" to "two", "three" to "four"),
            Tags("Tag", "Tagz")
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