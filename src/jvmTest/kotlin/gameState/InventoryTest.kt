package gameState


import core.DependencyInjector
import core.GameManager
import core.GameState
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsMock
import core.body.BodyManager
import core.body.BodysCollection
import core.body.BodysMock
import createItem
import createPouch
import inventory.Inventory
import kotlinx.coroutines.runBlocking
import traveling.location.location.LocationManager
import traveling.location.location.LocationsCollection
import traveling.location.location.LocationsMock
import traveling.location.network.NetworksCollection
import traveling.location.network.NetworksMock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InventoryTest {

    @BeforeTest
    fun setup() {
        runBlocking {
            DependencyInjector.setImplementation(BodysCollection::class, BodysMock())
            BodyManager.reset()

            val behaviorParser = BehaviorsMock()
            DependencyInjector.setImplementation(BehaviorsCollection::class, behaviorParser)
            BehaviorManager.reset()

            DependencyInjector.setImplementation(NetworksCollection::class, NetworksMock())
            DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
            LocationManager.reset()

            GameState.putPlayer(GameManager.newPlayer())
        }
    }

    @Test
    fun getItemIsNested() {
        runBlocking {
            val item = createItem(weight = 2)
            val pouch = createPouch(weight = 2)
            pouch.add(item)

            val inventory = Inventory()
            inventory.add(pouch, 100)

            assertEquals(item, inventory.getItem("Apple"))
        }
    }

    @Test
    fun existsIsNested() {
        runBlocking {
            val item = createItem(weight = 2)
            val pouch = createPouch(weight = 1)
            pouch.add(item)

            val inventory = Inventory()
            inventory.add(pouch, 100)

            assertTrue(inventory.exists(item))
        }
    }

    @Test
    fun removeItem() {
        runBlocking {
            val item = createItem(weight = 2)
            val inventory = Inventory()
            inventory.add(item, 100)
            inventory.remove(item)

            assertEquals(0, inventory.getAllItems().size)
        }
    }

    @Test
    fun removeNestedItem() {
        runBlocking {
            val item = createItem(weight = 2)
            val pouch = createPouch(weight = 1)
            pouch.add(item)

            val inventory = Inventory()
            inventory.add(pouch, 100)
            inventory.remove(item)

            assertEquals(1, inventory.getAllItems().size)
        }
    }

    @Test
    fun getWeightOfSingleItem() {
        runBlocking {
            val item = createItem(weight = 1)
            val inventory = Inventory()
            inventory.add(item, 100)
            assertEquals(1, inventory.getWeight())
        }
    }

    @Test
    fun getWeightIncludingNestedInventory() {
        runBlocking {
            val item = createItem(weight = 2)
            val pouch = createPouch(weight = 1)
            pouch.add(item)

            val inventory = Inventory()
            inventory.add(pouch, 100)
            assertEquals(3, inventory.getWeight())
        }
    }

    @Test
    fun getItemsDoesNotIncludeDuplicates() {
        runBlocking {
            val apple = createItem("Apple", weight = 1)
            val pear = createItem("pear", weight = 3)

            val inventory = Inventory()
            inventory.add(apple, 100)
            inventory.add(apple, 100)
            inventory.add(pear, 100)

            val items = inventory.getItems()

            assertEquals(2, items.size)
            assertTrue(items.contains(apple))
            assertEquals(2,apple.properties.getCount())
            assertTrue(items.contains(pear))
            assertEquals(5, inventory.getWeight())
        }
    }
}
