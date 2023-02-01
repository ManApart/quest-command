package crafting

import core.thing.Thing
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test


import status.stat.COOKING
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class RecipeResultTest {

    @Test
    fun basicBuild() {
        runBlocking {
            val recipeIngredient = ingredient {
                name("Apple")
                tag("Fruit", "Stale")
                skill(COOKING, 2)
                matches { _, _, _ -> true }
            }

            val recipeResult = result {
                reference("Apple")
                description("This is a test result")
                addTag("Tasty", "Fresh")
                removeTag("Stale")
            }.build()

            val base = Thing("Apple").also { it.properties.tags.add("Fruit", "Stale") }
            val crafter = Thing("Crafter")
            val result = recipeResult.getResult(crafter, null, mapOf("Apple" to Pair(recipeIngredient, base)))

            assertEquals("This is a test result", recipeResult.description)
            assertEquals(listOf("Fruit", "Tasty", "Fresh"), result.properties.tags.getAll())
            assertFalse(result.properties.tags.has("Stale"))
        }
    }

    @Test
    fun recipeErrorsIfNoNameOrReference() {
        assertThrows<IllegalStateException> {
            runBlocking {
                result {
                    description("This is a test result")
                    addTag("Tasty", "Fresh")
                    removeTag("Stale")
                }.build()
            }
        }
    }

    private fun ingredient(initializer: RecipeIngredientBuilder.() -> Unit): RecipeIngredient {
        return RecipeIngredientBuilder().apply(initializer).build()
    }
}