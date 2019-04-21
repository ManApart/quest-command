package crafting

import core.gameState.*
import core.gameState.Target
import core.gameState.location.LocationNode
import core.gameState.location.NOWHERE_NODE
import inventory.dropItem.TransferItem
import org.junit.Test
import system.*
import system.item.ItemManager
import system.item.ItemParser
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CraftTest {

    @Test
    fun recipeReturnsNewItems() {
        val baker = createBaker()
        val ingredient = Target("Apple")
        baker.inventory.add(ingredient)

        val recipe = Recipe("Apples of Silver and Gold", listOf(RecipeIngredient("Apple")), results = listOf(RecipeResult("Gold"), RecipeResult("Silver")))

        val resultItemName1 = "Silver"
        val resultItemName2 = "Gold"
        val fakeParser = ItemFakeParser(listOf(Target(resultItemName1), Target(resultItemName2)))
        DependencyInjector.setImplementation(ItemParser::class.java, fakeParser)
        ItemManager.reset()

        EventManager.registerListener(TransferItem())

        Craft().execute(CraftRecipeEvent(baker, recipe))

        EventManager.executeEvents()

        assertFalse(baker.inventory.exists(ingredient))
        assertNotNull(baker.inventory.getItem(resultItemName1))
        assertNotNull(baker.inventory.getItem(resultItemName2))
    }

    @Test
    fun recipeReturnsFirstItemWithDifferentTags() {
        val baker = createBaker()
        val ingredient = Target("Apple")
        baker.inventory.add(ingredient)

        val fakeParser = ItemFakeParser(listOf(ingredient))
        DependencyInjector.setImplementation(ItemParser::class.java, fakeParser)
        ItemManager.reset()

        EventManager.registerListener(TransferItem())

        val recipe = Recipe("Apples of Silver and Gold", listOf(RecipeIngredient("Apple")), results = listOf(RecipeResult(id = 0, tagsAdded = Tags(listOf("Cooked")))))
        Craft().execute(CraftRecipeEvent(baker, recipe))

        EventManager.executeEvents()

        val result = baker.inventory.getItem(ingredient.name)
        assertNotNull(result)
        assertTrue(result?.properties?.tags?.has("Cooked") ?: false)
    }

    private fun createBaker(): Target {
        val baker = Player(location = NOWHERE_NODE)
        val pouch = Target("Pouch", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "15"))))
        baker.inventory.add(pouch)
        return baker
    }
}