package gameState

import core.DependencyInjector
import core.GameManager
import core.GameState
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorParser
import core.body.BodyManager
import createItem
import createPouch
import inventory.Inventory
import org.junit.Before
import org.junit.Test
import system.BehaviorFakeParser
import system.BodyFakeParser
import system.location.LocationFakeParser
import traveling.location.location.LocationManager
import traveling.location.location.LocationParser
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class InventoryTest {

    @Before
    fun setup() {
        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
        BodyManager.reset()

        val behaviorParser = BehaviorFakeParser()
        DependencyInjector.setImplementation(BehaviorParser::class.java, behaviorParser)
        BehaviorManager.reset()

        val locationParser = LocationFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
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

}