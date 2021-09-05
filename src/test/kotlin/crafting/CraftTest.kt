package crafting

import core.DependencyInjector
import core.GameManager
import core.events.EventManager
import core.properties.Tags
import core.target.Target
import core.target.item.ItemManager
import core.target.item.ItemsCollection
import core.target.item.ItemsMock
import crafting.craft.Craft
import crafting.craft.CraftRecipeEvent
import createItem
import createItemBuilder
import createPouch
import createMockedGame
import inventory.pickupItem.TakeItem
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CraftTest {

    @Before
    fun setup() {
        createMockedGame()
    }

    @Test
    fun recipeReturnsNewItems() {
        val baker = createBaker()
        val ingredient = createItem("Apple")
        baker.inventory.add(ingredient)

        val recipe = Recipe("Apples of Silver and Gold", listOf(RecipeIngredient("Apple")), results = listOf(RecipeResult("Gold"), RecipeResult("Silver")))

        val resultItemName1 = "Silver"
        val resultItemName2 = "Gold"
        val fakeParser = ItemsMock(listOf(createItemBuilder(resultItemName1), createItemBuilder(resultItemName2)))
        DependencyInjector.setImplementation(ItemsCollection::class.java, fakeParser)
        ItemManager.reset()

        EventManager.registerListener(TakeItem())

        Craft().execute(CraftRecipeEvent(baker, recipe))

        EventManager.executeEvents()

        assertFalse(baker.inventory.exists(ingredient))
        assertNotNull(baker.inventory.getItem(resultItemName1))
        assertNotNull(baker.inventory.getItem(resultItemName2))
    }

    @Test
    fun recipeReturnsFirstItemWithDifferentTags() {
        val baker = createBaker()
        val ingredient = createItemBuilder("Apple")
        baker.inventory.add(ingredient.build())

        val fakeParser = ItemsMock(listOf(ingredient))
        DependencyInjector.setImplementation(ItemsCollection::class.java, fakeParser)
        ItemManager.reset()

        EventManager.registerListener(TakeItem())

        val recipe = Recipe("Apples of Silver and Gold", listOf(RecipeIngredient("Apple")), results = listOf(RecipeResult(id = 0, tagsAdded = Tags(listOf("Cooked")))))
        Craft().execute(CraftRecipeEvent(baker, recipe))

        EventManager.executeEvents()

        val result = baker.inventory.getItem(ingredient.name)
        assertNotNull(result)
        assertTrue(result.properties.tags.has("Cooked"))
    }

    private fun createBaker(): Target {
        val baker = GameManager.newPlayer()
        val pouch = createPouch(size = 30)
        baker.inventory.add(pouch)
        return baker
    }
}
