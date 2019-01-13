package core.gamestate

import core.gameState.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InventoryTest {

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