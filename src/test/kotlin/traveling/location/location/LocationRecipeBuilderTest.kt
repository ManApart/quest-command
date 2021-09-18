package traveling.location.location

import core.conditional.ConditionalStringPointer
import core.conditional.ConditionalStringType
import core.properties.Properties
import core.properties.Tags
import org.junit.Test
import kotlin.test.assertEquals


class LocationRecipeBuilderTest{

    @Test
    fun basicExtends(){
        val expected = LocationRecipe("Place", weather = ConditionalStringPointer("Inside Weather", ConditionalStringType.WEATHER), properties = Properties(tags = Tags("Inside", "Small")))

        val base = locationRecipe("Inside"){
            props { tag("Inside") }
            weather("Inside Weather")
        }

        val location = locationRecipe("Place"){
            extends("Inside")
            props { tag("Small") }
        }

        val actual = listOf(base, location).build().last()

        assertEquals(expected.properties, actual.properties)
        assertEquals(expected.weather, actual.weather)
    }
}