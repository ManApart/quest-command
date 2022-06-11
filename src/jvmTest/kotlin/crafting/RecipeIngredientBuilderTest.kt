package crafting

import core.thing.Thing
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test


import status.stat.COOKING
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue


class RecipeIngredientBuilderTest {

    @Test
    fun basicBuild() {
        val recipeIngredient = ingredient {
            name("Apple")
            tag("Fruit")
            skill(COOKING, 2)
            toolProps {
                tag("Sharp")
            }
            matches { crafter, _, _ ->
                crafter.name.lowercase() == "Bob the Baker".lowercase()
            }
        }

        val baker = Thing("Bob the Baker").also { it.soul.addStat(COOKING, 2) }
        val thief = Thing("Thief").also { it.soul.addStat(COOKING, 2) }
        val tool = Thing("Knife").also { it.properties.tags.add("Sharp") }
        val ingredients = listOf(Thing("Apple").also { it.properties.tags.add("Fruit") })

        assertNotNull(recipeIngredient.findMatchingIngredient(baker, ingredients, tool))
        assertNull(recipeIngredient.findMatchingIngredient(thief, ingredients, tool))
    }

    @Test
    fun recipeErrorsIfNoCriteria() {
        assertThrows<IllegalArgumentException> {
            ingredient {}
        }
    }

    @Test
    fun recipeErrorsIfNoNameAndEmptyTags() {
        assertThrows<IllegalArgumentException> {
            ingredient {
                tag()
            }
        }
    }

    @Test
    fun recipeErrorsIfBlankNameAndNoTags() {
        assertThrows<IllegalArgumentException> {
            ingredient {
                name(" ")
            }
        }
    }

    @Test
    fun noErrorWithName() {
        val item = ingredient {
            name("Ingredient")
        }
        val crafter = Thing("Crafter")
        val match = Thing("Ingredient")
        val noMatch = Thing("Ingredient2")

        assertTrue(item.matches(crafter, match, null))
        assertFalse(item.matches(crafter, noMatch, null))
    }

    @Test
    fun noErrorWithTags() {
        val item = ingredient {
            tag("Tag")
        }
        val crafter = Thing("Crafter")
        val match = Thing("Ingredient").also {
            it.properties.tags.add("Tag")
        }
        val noMatch = Thing("Ingredient2")

        assertTrue(item.matches(crafter, match, null))
        assertFalse(item.matches(crafter, noMatch, null))
    }

    private fun ingredient(initializer: RecipeIngredientBuilder.() -> Unit): RecipeIngredient {
        return RecipeIngredientBuilder().apply(initializer).build()
    }

}