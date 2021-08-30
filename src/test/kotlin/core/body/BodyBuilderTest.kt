package core.body

import core.properties.Properties
import core.properties.Tags
import core.properties.Values
import org.junit.Test
import traveling.location.location.LocationRecipe
import kotlin.test.assertEquals

class BodyBuilderTest {
    @Test
    fun basicBuild() {
        val expected = Body(
            "Grain Chute", LocationRecipe(
                "Grain Chute",
                properties = Properties(Values(mapOf("size" to "3")), Tags("Container"))
            )
        )

        val actual = body("Grain Chute") {
            part {
                props {
                    value("size", 3)
                    tag("Person")
                }
            }
        }
        assertEquals(expected, actual)
    }

    @Test
    fun basicWithDifferentName() {
        val expected = Body(
            "Body", LocationRecipe(
                "Other Name",
                properties = Properties(Values(mapOf("size" to "3")), Tags("Container"))
            )
        )

        val actual = body("Body") {
            part("Other Name") {
                props {
                    value("size", 3)
                    tag("Person")
                }
            }
        }
        assertEquals(expected, actual)
    }
}