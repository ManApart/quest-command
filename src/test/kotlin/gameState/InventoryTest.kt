package gameState

import core.DependencyInjector
import core.GameManager
import core.GameState
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsMock
import core.body.*
import createItem
import createPouch
import inventory.Inventory
import org.junit.Before
import org.junit.Test
import traveling.location.location.*
import traveling.location.network.NetworksCollection
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class InventoryTest {

    @Before
    fun setup() {
        DependencyInjector.setImplementation(BodysCollection::class, BodysMock())
        DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock())
        BodyManager.reset()

        val behaviorParser = BehaviorsMock()
        DependencyInjector.setImplementation(BehaviorsCollection::class, behaviorParser)
        BehaviorManager.reset()

        DependencyInjector.setImplementation(NetworksCollection::class, NetworksMock())
        DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
        LocationManager.reset()

        GameState.player = GameManager.newPlayer()
    }

    @Test
    fun getItemIsNested() {
        val item = createItem(weight = 2)
        val pouch = createPouch(weight = 2)
        pouch.inventory.add(item)

        val inventory = Inventory()
        inventory.add(pouch)

        assertEquals(item, inventory.getItem("Apple"))
    }

    @Test
    fun existsIsNested() {
        val item = createItem(weight = 2)
        val pouch = createPouch(weight = 1)
        pouch.inventory.add(item)

        val inventory = Inventory()
        inventory.add(pouch)

        assertTrue(inventory.exists(item))
    }

    @Test
    fun removeItem() {
        val item = createItem(weight = 2)
        val inventory = Inventory()
        inventory.add(item)
        inventory.remove(item)

        assertEquals(0, inventory.getAllItems().size)
    }

    @Test
    fun removeNestedItem() {
        val item = createItem(weight = 2)
        val pouch = createPouch(weight = 1)
        pouch.inventory.add(item)

        val inventory = Inventory()
        inventory.add(pouch)
        inventory.remove(item)

        assertEquals(1, inventory.getAllItems().size)
    }

    @Test
    fun getWeightOfSingleItem() {
        val item = createItem(weight = 1)
        val inventory = Inventory()
        inventory.add(item)
        assertEquals(1, inventory.getWeight())
    }

    @Test
    fun getWeightIncludingNestedInventory() {
        val item = createItem(weight = 2)
        val pouch = createPouch(weight = 1)
        pouch.inventory.add(item)

        val inventory = Inventory()
        inventory.add(pouch)
        assertEquals(3, inventory.getWeight())
    }

    @Test
    fun getItemsDoesNotIncludeDuplicates() {
        val apple = createItem("Apple", weight = 1)
        val pear = createItem("pear", weight = 2)

        val rightHand = LocationRecipe("Right Hand")
        val leftHand = LocationRecipe("Left Hand")
        val body = createBody(listOf(rightHand, leftHand))
        val inventory = Inventory("Inventory", body)

        body.getParts().first().addTarget(apple)
        body.getParts().last().addTarget(apple)
        body.getParts().first().addTarget(pear)

        assertEquals(2, inventory.getItems().size)
        assertTrue(inventory.getItems().contains(apple))
        assertTrue(inventory.getItems().contains(pear))
        assertEquals(3, inventory.getWeight())
    }

    @Test
    fun thing() {
        val inventory = Inventory()
        val item = createItem()
        inventory.add(item)
        inventory.getAllItems()
    }

}