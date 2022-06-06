package crafting

import core.DependencyInjector
import core.GameManager
import core.Player
import core.events.*
import core.thing.item.ItemManager
import core.thing.item.ItemsCollection
import core.thing.item.ItemsMock
import crafting.craft.Craft
import crafting.craft.CraftRecipeEvent
import createItem
import createItemBuilder
import createMockedGame
import createPouch
import inventory.pickupItem.TakeItem
import inventory.pickupItem.TakeItemEvent

import kotlin.test.BeforeTest


import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.Test

class CraftTest {

    @BeforeTest
    fun setup() {
        createMockedGame()
    }

    @Test
    fun recipeReturnsNewItems() {
        val baker = createBaker()
        val ingredient = createItem("Apple")
        baker.inventory.add(ingredient)

        val recipe = Recipe("Apples of Silver and Gold", mapOf("Apple" to RecipeIngredient("Apple")), results = listOf(RecipeResult("Gold"), RecipeResult("Silver")))

        val resultItemName1 = "Silver"
        val resultItemName2 = "Gold"
        val craftEvent = CraftRecipeEvent(baker, recipe)

        val fakeParser = ItemsMock(listOf(createItemBuilder(resultItemName1), createItemBuilder(resultItemName2)))
        DependencyInjector.setImplementation(ItemsCollection::class, fakeParser)
        ItemManager.reset()

        DependencyInjector.setImplementation(EventListenerMapCollection::class, EventListenerMapGenerated())
        EventManager.reset()

        Craft().execute(craftEvent)
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
        val recipe = Recipe("Apples of Silver and Gold", mapOf("Fruit" to RecipeIngredient("Apple")), results = listOf(RecipeResult("Fruit", listOf("Cooked"), listOf())))
        val craftEvent = CraftRecipeEvent(baker, recipe)

        val fakeParser = ItemsMock(listOf(ingredient))
        DependencyInjector.setImplementation(ItemsCollection::class, fakeParser)
        ItemManager.reset()

        DependencyInjector.setImplementation(EventListenerMapCollection::class, EventListenerMapGenerated())
        EventManager.reset()

        Craft().execute(craftEvent)

        EventManager.executeEvents()

        val result = baker.inventory.getItem(ingredient.name)
        assertNotNull(result)
        assertTrue(result.properties.tags.has("Cooked"))
    }

    private fun createBaker(): Player {
        val baker = GameManager.newPlayer()
        val pouch = createPouch(size = 30)
        baker.thing.inventory.add(pouch)
        return baker
    }
}
