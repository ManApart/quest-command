package crafting

import core.gameState.Item
import core.gameState.Player
import core.gameState.Tags
import inventory.pickupItem.PickupItem
import org.junit.Test
import system.*
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CraftTest {

    @Test
    fun recipeReturnsNewItems() {
        val baker = Player().creature
        val ingredient = Item("Apple")
        baker.inventory.add(ingredient)

        val recipe = Recipe("Apples of Silver and Gold", listOf(RecipeIngredient("Apple")), results = listOf(RecipeResult("Gold"), RecipeResult("Silver")))

        val resultItemName1 = "Silver"
        val resultItemName2 = "Gold"
        val fakeParser = ItemFakeParser(listOf(Item(resultItemName1), Item(resultItemName2)))
        DependencyInjector.setImplementation(ItemParser::class.java, fakeParser)
        ItemManager.reset()

        EventManager.registerListener(PickupItem())

        Craft().execute(CraftRecipeEvent(baker, recipe))

        EventManager.executeEvents()

        assertFalse(baker.inventory.exists(ingredient))
        assertNotNull(baker.inventory.getItem(resultItemName1))
        assertNotNull(baker.inventory.getItem(resultItemName2))
    }

    @Test
    fun recipeReturnsFirstItemWithDifferentTags() {
        val baker = Player().creature
        val ingredient = Item("Apple")
        baker.inventory.add(ingredient)

        val fakeParser = ItemFakeParser(listOf(ingredient))
        DependencyInjector.setImplementation(ItemParser::class.java, fakeParser)
        ItemManager.reset()

        EventManager.registerListener(PickupItem())

        val recipe = Recipe("Apples of Silver and Gold", listOf(RecipeIngredient("Apple")), results = listOf(RecipeResult(id = 0, tagsAdded = Tags(listOf("Cooked")))))
        Craft().execute(CraftRecipeEvent(baker, recipe))

        EventManager.executeEvents()

        assertTrue(baker.inventory.exists(ingredient))
        assertTrue(ingredient.properties.tags.has("Cooked"))
    }
}