package core.gamestate

import core.commands.CommandParser
import core.gameState.*
import core.gameState.body.ProtoBody
import core.gameState.location.LocationNode
import core.utility.NameSearchableList
import org.junit.Before
import org.junit.Test
import system.BehaviorFakeParser
import system.BodyFakeParser
import system.DependencyInjector
import system.EventManager
import system.behavior.BehaviorManager
import system.behavior.BehaviorParser
import system.body.BodyManager
import system.body.BodyParser
import system.location.LocationFakeParser
import system.location.LocationManager
import system.location.LocationParser
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InventoryTest {

    @Before
    fun setup() {
        val bodyParser = BodyFakeParser(listOf(ProtoBody("Human")))
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        val behaviorParser = BehaviorFakeParser()
        DependencyInjector.setImplementation(BehaviorParser::class.java, behaviorParser)
        BehaviorManager.reset()

        val locationParser = LocationFakeParser(locationNodes = NameSearchableList(listOf(LocationNode("an open field"))))
        DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
        LocationManager.reset()

        GameState.player = Player(Creature("Player", "Player"))
    }

    @Test
    fun getItemIsNested(){
        val item = Item("Apple", weight = 2)
        val pouch = Item("Pouch", weight = 1)
        pouch.inventory.add(item)

        val inventory = Inventory()
        inventory.add(pouch)

        assertEquals(item, inventory.getItem("Apple"))
    }

    @Test
    fun existsIsNested(){
        val item = Item("Apple", weight = 2)
        val pouch = Item("Pouch", weight = 1)
        pouch.inventory.add(item)

        val inventory = Inventory()
        inventory.add(pouch)

        assertTrue(inventory.exists(item))
    }

    @Test
    fun removeItem(){
        val item = Item("Apple", weight = 2)
        val inventory = Inventory()
        inventory.add(item)
        inventory.remove(item)

        assertEquals(0, inventory.getAllItems().size)
    }

    @Test
    fun removeNestedItem(){
        val item = Item("Apple", weight = 2)
        val pouch = Item("Pouch", weight = 1)
        pouch.inventory.add(item)

        val inventory = Inventory()
        inventory.add(pouch)
        inventory.remove(item)

        assertEquals(1, inventory.getAllItems().size)
    }

    @Test
    fun getWeightOfSingleItem(){
        val item = Item("Apple", weight = 1)
        val inventory = Inventory()
        inventory.add(item)
        assertEquals(1, inventory.getWeight())
    }

    @Test
    fun getWeightIncludingNestedInventory(){
        val item = Item("Apple", weight = 2)
        val pouch = Item("Pouch", weight = 1)
        pouch.inventory.add(item)

        val inventory = Inventory()
        inventory.add(pouch)
        assertEquals(3, inventory.getWeight())
    }

    @Test
    fun hasRoomForItem(){
        val item = Item("Apple", weight = 2)
        val inventory = Inventory()
        assertTrue(inventory.hasCapacityFor(item, 3))
    }

    @Test
    fun doesNotHaveRoomForItem(){
        val item = Item("Apple", weight = 5)
        val inventory = Inventory()
        assertFalse(inventory.hasCapacityFor(item, 3))
    }

    @Test
    fun findItemsWithCapacityAndTypeForItem(){
        val searchItem = Item("Apple", properties = Properties(tags = Tags(listOf("Raw"))))
        val pouch = Item("Pouch", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5"))))
        val inventory = Inventory()
        inventory.add(pouch)
        inventory.add(Item("Noise"))

        val results = inventory.findSubInventoryFor(searchItem)
        assertEquals(1, results.size)
        assertEquals(pouch, results[0])
    }
}