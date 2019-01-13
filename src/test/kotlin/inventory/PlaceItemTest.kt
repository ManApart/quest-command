package inventory

import combat.battle.position.TargetPosition
import core.gameState.*
import core.gameState.body.Body
import core.gameState.body.BodyPart
import interact.scope.ScopeManager
import inventory.dropItem.TransferItem
import inventory.dropItem.TransferItemEvent
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlaceItemTest {
    //TODO - assert not null

    @Test
    fun dropItem() {
        val creature = Creature("Name", "")
        val item = Item("Apple")
        creature.inventory.add(item)
        val scope = ScopeManager.getScope(creature.location)

        TransferItem().execute(TransferItemEvent(item, creature))
        assertTrue(scope.getTargets(item.name).isNotEmpty())
        assertFalse(creature.inventory.getItem(item.name) != null)
    }

    @Test
    fun placeItemInActivatorContainer() {
        val item = Item("Apple", weight = 5)
        val creature = Creature("Name", "")
        val chest = Activator("name", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5"))))
        creature.inventory.add(item)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertTrue(chest.creature.inventory.getItem(item.name) != null)
        assertFalse(creature.inventory.getItem(item.name) != null)
    }

    @Test
    fun placeItemInCreatureContainerSubInventory() {
        //Capacity = 10 * Strength
        val item = Item("Apple", weight = 10)
        val creature = Creature("Name", "")
        creature.inventory.add(item)

        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), stats = Values(mapOf("Strength" to "1"))))
        val pouch = Item("Pouch", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "15"))))
        chest.inventory.add(pouch)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertTrue(chest.inventory.getItem(item.name) != null)
        assertFalse(creature.inventory.getItem(item.name) != null)
    }

    @Test
    fun placeItemInCreatureContainerEquip() {
        val item = Item("Dagger", equipSlots = listOf(listOf("Grip")))
        val creature = Creature("Name", "")
        creature.inventory.add(item)

        val part = BodyPart("Hand", TargetPosition(), listOf("Grip", "Glove"))
        val body = Body(parts = listOf(part))
        val chest = Creature("Name", "", body = body, properties = Properties(Tags(listOf("Container", "Open")), stats = Values(mapOf("Strength" to "1"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertTrue(chest.inventory.getItem(item.name) != null)
        assertFalse(creature.inventory.getItem(item.name) != null)
    }

    @Test
    fun placeItemInItemContainer() {
        val item = Item("Apple", weight = 5)
        val creature = Creature("Name", "")
        val chest = Activator("name", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5"))))
        creature.inventory.add(item)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertTrue(chest.creature.inventory.getItem(item.name) != null)
        assertFalse(creature.inventory.getItem(item.name) != null)
    }

    @Test
    fun placeItemStackInContainer() {
        val item = Item("Apple", weight = 5, count = 2)
        val creature = Creature("Name", "")
        val chest = Activator("Name", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5"))))
        creature.inventory.add(item)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertTrue(chest.creature.inventory.getItem(item.name) != null)
        assertEquals(1, chest.creature.inventory.getItem(item.name)!!.count)

        assertTrue(creature.inventory.getItem(item.name) != null)
        assertEquals(1, creature.inventory.getItem(item.name)!!.count)
    }

    @Test
    fun doNotPlaceInNonContainer() {
        val item = Item("Apple", weight = 1)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Open")), Values(mapOf("Capacity" to "5"))))
        creature.inventory.add(item)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertTrue(creature.inventory.getItem(item.name) != null)
        assertFalse(chest.inventory.getItem(item.name) != null)
    }

    @Test
    fun doNotPlaceInNonOpenContainer() {
        val item = Item("Apple", weight = 1)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container")), Values(mapOf("Capacity" to "5"))))
        creature.inventory.add(item)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertTrue(creature.inventory.getItem(item.name) != null)
        assertFalse(chest.inventory.getItem(item.name) != null)
    }

    @Test
    fun doNotPlaceInFullContainer() {
        val item = Item("Apple", weight = 5)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "1"))))
        creature.inventory.add(item)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertTrue(creature.inventory.getItem(item.name) != null)
        assertFalse(chest.inventory.getItem(item.name) != null)
    }

    @Test
    fun doNotPlaceInContainerWithoutCapacity() {
        val item = Item("Apple", weight = 1)
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open"))))
        creature.inventory.add(item)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertTrue(creature.inventory.getItem(item.name) != null)
        assertFalse(chest.inventory.getItem(item.name) != null)
    }

    @Test
    fun placeItemInContainerThatHasASingleMatchingTag() {
        val item = Item("Apple", weight = 1, properties = Properties(Tags(listOf("Food", "Fruit"))))
        val creature = Creature("Name", "")
        val chest = Activator("Name", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5", "CanHold" to "Food,Apparel"))))
        creature.inventory.add(item)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertTrue(chest.creature.inventory.getItem(item.name) != null)
        assertFalse(creature.inventory.getItem(item.name) != null)
    }

    @Test
    fun doNotPlaceItemInContainerThatHasNoMatchingTags() {
        val item = Item("Apple", weight = 1, properties = Properties(Tags(listOf("Food", "Fruit"))))
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5", "CanHold" to "Weapon,Apparel"))))
        creature.inventory.add(item)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertTrue(creature.inventory.getItem(item.name) != null)
        assertFalse(chest.inventory.getItem(item.name) != null)
    }


}