package gameState


import core.properties.ValueStrings.CAPACITY
import core.thing.Thing
import core.thing.thing
import createItem
import createPouch
import inventory.Inventory
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InventoryTest {

    private lateinit var holder: Thing

    @BeforeTest
    fun setup() {
        runBlocking {
            holder = thing("Holder") {
                body("Test") {
                    dimensions(100, 100, 100)
                }
                props {
                    value(CAPACITY, 100)
                }
            }.build()
        }
    }

    @Test
    fun getItemIsNested() {
        runBlocking {
            val item = createItem(weight = 2)
            val pouch = createPouch(weight = 2)
            pouch.add(item)

            val inventory = Inventory()
            inventory.add(holder, pouch)

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
            inventory.add(holder, pouch)

            assertTrue(inventory.exists(item))
        }
    }

    @Test
    fun removeItem() {
        runBlocking {
            val item = createItem(weight = 2)
            val inventory = Inventory()
            inventory.add(holder, item)
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
            inventory.add(holder, pouch)
            inventory.remove(item)

            assertEquals(1, inventory.getAllItems().size)
        }
    }

    @Test
    fun getWeightOfSingleItem() {
        runBlocking {
            val item = createItem(weight = 1)
            val inventory = Inventory()
            inventory.add(holder, item)
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
            inventory.add(holder, pouch)
            assertEquals(3, inventory.getWeight())
        }
    }

    @Test
    fun getItemsDoesNotIncludeDuplicates() {
        runBlocking {
            val apple = createItem("Apple", weight = 1)
            val pear = createItem("pear", weight = 3)

            val inventory = Inventory()
            inventory.add(holder, apple)
            inventory.add(holder, apple)
            inventory.add(holder, pear)

            val items = inventory.getItems()

            assertEquals(2, items.size)
            assertTrue(items.contains(apple))
            assertEquals(2, apple.properties.getCount())
            assertTrue(items.contains(pear))
            assertEquals(5, inventory.getWeight())
        }
    }
}
