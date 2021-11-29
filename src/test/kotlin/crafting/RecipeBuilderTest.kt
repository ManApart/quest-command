package crafting

import core.properties.Properties
import core.properties.Tags
import org.junit.Test
import kotlin.test.assertEquals


class RecipeBuilderTest {

    @Test
    fun basicBuild() {
        val expected = Recipe(
            "Sliced Food",
            mapOf("Fruit" to RecipeIngredient(Tags("Food", "Slicable"))),
            mapOf("Cooking" to 1),
            Properties(tags = Tags("Sharp")),
            listOf(RecipeResult(id = 0, tagsAdded = Tags("Sliced"), tagsRemoved = Tags("Slicable"))),
            "slice"
        )


        val actual = recipe("Sliced Food") {
            verb("slice")
            skill("Cooking", 1)
            ingredient( "Fruit","Food", "Slicable")
            toolProps {
                tag("Sharp")
            }
            result {
                id(0)
                addTag("Sliced")
                removeTag("Slicable")
            }

        }.build()

        assertEquals(expected, actual)
    }

}