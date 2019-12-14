package crafting

import core.properties.Tags
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RecipeIngredientTest {


    @Test(expected = IllegalArgumentException::class)
    fun recipeErrorsIfNeitherNameNorTagsAreGiven() {
        RecipeIngredient()
    }

    @Test(expected = IllegalArgumentException::class)
    fun recipeErrorsIfNoNameAndEmptyTags() {
        RecipeIngredient(tags = Tags())
    }

    @Test(expected = IllegalArgumentException::class)
    fun recipeErrorsIfBlankNameAndNoTags() {
        RecipeIngredient(" ")
    }

    @Test
    fun noErrorWithName() {
        val recipe = RecipeIngredient("Ingredient")
        assertEquals("Ingredient", recipe.name)
    }

    @Test
    fun noErrorWithTags() {
        val recipe = RecipeIngredient(tags = Tags(listOf("Tag")))
        assertTrue(recipe.tags.has("Tag"))
    }
}