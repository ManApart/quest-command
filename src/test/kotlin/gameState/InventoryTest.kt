package gameState

import core.DependencyInjector
import core.GameManager
import core.GameState
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorParser
import core.body.BodyManager
import core.properties.Properties
import core.properties.Tags
import core.properties.Values
import core.target.Target
import inventory.Inventory
import org.junit.Before
import org.junit.Test
import system.BehaviorFakeParser
import system.BodyFakeParser
import system.location.LocationFakeParser
import traveling.location.location.LocationManager
import traveling.location.location.LocationParser
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
    fun getItemIsNested(){
        val item = createItem("Apple", weight = 2)
        val pouch = createItem("Pouch", weight = 1)
        pouch.inventory.add(item)

        val inventory = Inventory()
        inventory.add(pouch)

        assertEquals(item, inventory.getItem("Apple"))
    }

    @Test
    fun existsIsNested(){
        val item = createItem("Apple", 2)
        val pouch = createItem("Pouch", weight = 1)
        pouch.inventory.add(item)

        val inventory = Inventory()
        inventory.add(pouch)

        assertTrue(inventory.exists(item))
    }

    @Test
    fun removeItem(){
        val item = createItem("Apple", weight = 2)
        val inventory = Inventory()
        inventory.add(item)
        inventory.remove(item)

        assertEquals(0, inventory.getAllItems().size)
    }

    @Test
    fun removeNestedItem(){
        val item = createItem("Apple", weight = 2)
        val pouch = createItem("Pouch", weight = 1)
        pouch.inventory.add(item)

        val inventory = Inventory()
        inventory.add(pouch)
        inventory.remove(item)

        assertEquals(1, inventory.getAllItems().size)
    }

    @Test
    fun getWeightOfSingleItem(){
        val item = createItem("Apple", weight = 1)
        val inventory = Inventory()
        inventory.add(item)
        assertEquals(1, inventory.getWeight())
    }

    @Test
    fun getWeightIncludingNestedInventory(){
        val item = createItem("Apple", weight = 2)
        val pouch = createItem("Pouch", weight = 1)
        pouch.inventory.add(item)

        val inventory = Inventory()
        inventory.add(pouch)
        assertEquals(3, inventory.getWeight())
    }

    @Test
    fun hasRoomForItem(){
        val item = createItem("Apple", weight = 2)
        val inventory = Inventory()
        assertTrue(inventory.hasCapacityFor(item, 3))
    }

    @Test
    fun doesNotHaveRoomForItem(){
        val item = createItem("Apple", weight = 5)
        val inventory = Inventory()
        assertFalse(inventory.hasCapacityFor(item, 3))
    }

    @Test
    fun findItemsWithCapacityAndTypeForItem(){
        val searchItem = Target("Apple", properties = Properties(tags = Tags(listOf("Raw"))))
        val pouch = Target("Pouch", properties = Properties(Values(mapOf("Capacity" to "5")), Tags(listOf("Container", "Open"))))
        val inventory = Inventory()
        inventory.add(pouch)
        inventory.add(createItem("Noise", 0))

        val results = inventory.findSubInventoryFor(searchItem)
        assertEquals(1, results.size)
        assertEquals(pouch, results[0])
    }

    private fun createItem(name: String, weight: Int) : Target {
        return Target(name, properties = Properties(Values(mapOf("weight" to weight.toString()))))
    }

}