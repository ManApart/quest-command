package crafting

import core.properties.Properties
import core.properties.Tags
import org.junit.Test
import status.stat.COOKING
import kotlin.test.assertEquals


class RecipeBuilderTest {

    @Test
    fun basicBuild() {
        val expected = Recipe(
            "Sliced Food",
            mapOf("Fruit" to RecipeIngredient(Tags("Food", "Slicable"))),
            mapOf(COOKING to 1),
            Properties(tags = Tags("Sharp")),
            listOf(RecipeResult("Fruit", listOf("Sliced"), listOf("Slicable"))),
            "slice"
        )


        val actual = recipe("Sliced Food") {
            verb("slice")
            skill(COOKING, 1)
            ingredient( "Fruit") {
                tag("Food", "Slicable")
            }
            toolProps {
                tag("Sharp")
            }
            result {
                reference("Fruit")
                addTag("Sliced")
                removeTag("Slicable")
            }

        }.build()

        assertEquals(expected, actual)
    }

}