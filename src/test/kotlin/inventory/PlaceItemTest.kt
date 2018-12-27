package inventory

import core.gameState.*
import interact.scope.ScopeManager
import inventory.dropItem.PlaceItem
import inventory.dropItem.PlaceItemEvent
import org.junit.Test
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
        assertTrue(scope.targetExists(item))
        assertFalse(creature.inventory.exists(item))
    }

    @Test
    fun placeItemInContainer() {
        val item = Item("Apple", weight = 5)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(chest.inventory.exists(item))
        assertFalse(creature.inventory.exists(item))
    }

    @Test
    fun placeItemInContainerByStrength() {
        //Capacity = 10 * Strength
        val item = Item("Apple", weight = 10)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5")), Values(mapOf("Strength" to "1"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(chest.inventory.exists(item))
        assertFalse(creature.inventory.exists(item))
    }

    @Test
    fun doNotPlaceInNonContainer() {
        val item = Item("Apple", weight = 1)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Open")), Values(mapOf("Capacity" to "5"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(creature.inventory.exists(item))
        assertFalse(chest.inventory.exists(item))
    }

    @Test
    fun doNotPlaceInNonOpenContainer() {
        val item = Item("Apple", weight = 1)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container")), Values(mapOf("Capacity" to "5"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(creature.inventory.exists(item))
        assertFalse(chest.inventory.exists(item))
    }

    @Test
    fun doNotPlaceInFullContainer() {
        val item = Item("Apple", weight = 5)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "1"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(creature.inventory.exists(item))
        assertFalse(chest.inventory.exists(item))
    }

    @Test
    fun doNotPlaceInContainerWithoutCapacity() {
        val item = Item("Apple", weight = 1)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(creature.inventory.exists(item))
        assertFalse(chest.inventory.exists(item))
    }

    @Test
    fun placeItemInContainerThatHasASingleMatchingTag() {
        val item = Item("Apple", weight = 1, properties = Properties(Tags(listOf("Food", "Fruit"))))
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5", "CanHold" to "Food,Apparel"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(chest.inventory.exists(item))
        assertFalse(creature.inventory.exists(item))
    }

    @Test
    fun doNotPlaceItemInContainerThatHasNoMatchingTags() {
        val item = Item("Apple", weight = 1, properties = Properties(Tags(listOf("Food", "Fruit"))))
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5", "CanHold" to "Weapon,Apparel"))))
        creature.inventory.add(item)

        PlaceItem().execute(PlaceItemEvent(creature, item, chest))

        assertTrue(creature.inventory.exists(item))
        assertFalse(chest.inventory.exists(item))
    }


}