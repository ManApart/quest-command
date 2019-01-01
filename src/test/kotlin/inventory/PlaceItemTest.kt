package inventory

import core.gameState.*
import interact.scope.ScopeManager
import inventory.dropItem.PlaceItem
import inventory.dropItem.PlaceItemEvent
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlaceItemTest {

    @Test
    fun dropItem() {
        val creature = Creature("Name", "")
        val item = Item("Apple")
        creature.inventory.add(item)
        val scope = ScopeManager.getScope(creature.location)

        PlaceItem().execute(PlaceItemEvent(creature, item))
        assertTrue(scope.getTargets(item.name).isNotEmpty())
        assertFalse(creature.inventory.getItem(item.name) != null)
    }

    @Test
    fun placeItemInContainer() {
        val item = Item("Apple", weight = 5)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(chest.inventory.getItem(item.name) != null)
        assertFalse(creature.inventory.getItem(item.name) != null)
    }
    
    @Test
    fun placeItemStackInContainer() {
        val item = Item("Apple", weight = 5, count = 2)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(chest.inventory.getItem(item.name) != null)
        assertEquals(1, chest.inventory.getItem(item.name)!!.count)

        assertTrue(creature.inventory.getItem(item.name) != null)
        assertEquals(1, creature.inventory.getItem(item.name)!!.count)
    }

    @Test
    fun placeItemInContainerByStrength() {
        //Capacity = 10 * Strength
        val item = Item("Apple", weight = 10)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5")), Values(mapOf("Strength" to "1"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(chest.inventory.getItem(item.name) != null)
        assertFalse(creature.inventory.getItem(item.name) != null)
    }

    @Test
    fun doNotPlaceInNonContainer() {
        val item = Item("Apple", weight = 1)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Open")), Values(mapOf("Capacity" to "5"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(creature.inventory.getItem(item.name) != null)
        assertFalse(chest.inventory.getItem(item.name) != null)
    }

    @Test
    fun doNotPlaceInNonOpenContainer() {
        val item = Item("Apple", weight = 1)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container")), Values(mapOf("Capacity" to "5"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(creature.inventory.getItem(item.name) != null)
        assertFalse(chest.inventory.getItem(item.name) != null)
    }

    @Test
    fun doNotPlaceInFullContainer() {
        val item = Item("Apple", weight = 5)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "1"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(creature.inventory.getItem(item.name) != null)
        assertFalse(chest.inventory.getItem(item.name) != null)
    }

    @Test
    fun doNotPlaceInContainerWithoutCapacity() {
        val item = Item("Apple", weight = 1)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(creature.inventory.getItem(item.name) != null)
        assertFalse(chest.inventory.getItem(item.name) != null)
    }

    @Test
    fun placeItemInContainerThatHasASingleMatchingTag() {
        val item = Item("Apple", weight = 1, properties = Properties(Tags(listOf("Food", "Fruit"))))
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5", "CanHold" to "Food,Apparel"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(chest.inventory.getItem(item.name) != null)
        assertFalse(creature.inventory.getItem(item.name) != null)
    }

    @Test
    fun doNotPlaceItemInContainerThatHasNoMatchingTags() {
        val item = Item("Apple", weight = 1, properties = Properties(Tags(listOf("Food", "Fruit"))))
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5", "CanHold" to "Weapon,Apparel"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(creature.inventory.getItem(item.name) != null)
        assertFalse(chest.inventory.getItem(item.name) != null)
    }


}