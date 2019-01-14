package inventory

import combat.battle.position.TargetPosition
import core.gameState.*
import core.gameState.body.Body
import core.gameState.body.BodyPart
import interact.scope.ScopeManager
import inventory.dropItem.TransferItem
import inventory.dropItem.TransferItemEvent
import org.junit.Test
import kotlin.test.*

class PlaceItemTest {

    @Test
    fun dropItem() {
        val creature = Creature("Creature")
        val item = Item("Apple")
        creature.inventory.add(item)
        val scope = ScopeManager.getScope(creature.location)

        TransferItem().execute(TransferItemEvent(item, creature))
        assertTrue(scope.getTargets(item.name).isNotEmpty())
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInActivatorContainer() {
        val item = Item("Apple", weight = 5)
        val creature = Creature("Creature", properties = Properties(Tags(listOf("Container", "Open"))))
        creature.inventory.add(item)

        val chest = Activator("Chest", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(chest.creature.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInCreatureContainerSubInventory() {
        //Capacity = 10 * Strength
        val creature = Creature("Creature", properties = Properties(Tags(listOf("Container", "Open"))))
        val item = Item("Apple", weight = 10)
        creature.inventory.add(item)

        val chest = Creature("Chest", properties = Properties(Tags(listOf("Container", "Open")), stats = Values(mapOf("Strength" to "1"))))
        val pouch = Item("Pouch", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "15"))))
        chest.inventory.add(pouch)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInCreatureContainerEquip() {
        val creature = Creature("Creature", properties = Properties(Tags(listOf("Container", "Open"))))
        val item = Item("Dagger", equipSlots = listOf(listOf("Grip")))
        creature.inventory.add(item)

        val part = BodyPart("Hand", TargetPosition(), listOf("Grip", "Glove"))
        val body = Body(parts = listOf(part))
        val chest = Creature("Chest", body = body, properties = Properties(Tags(listOf("Container", "Open")), stats = Values(mapOf("Strength" to "1"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInItemContainer() {
        val creature = Creature("Creature", properties = Properties(Tags(listOf("Container", "Open"))))
        val item = Item("Apple", weight = 5)
        creature.inventory.add(item)

        val chest = Item("Chest", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemStackInContainer() {
        val creature = Creature("Creature", properties = Properties(Tags(listOf("Container", "Open"))))
        val item = Item("Apple", weight = 5, count = 2)
        creature.inventory.add(item)

        val chest = Activator("Chest", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(chest.creature.inventory.getItem(item.name))
        assertEquals(1, chest.creature.inventory.getItem(item.name)!!.count)

        assertNotNull(creature.inventory.getItem(item.name))
        assertEquals(1, creature.inventory.getItem(item.name)!!.count)
    }

    @Test
    fun doNotPlaceInNonContainer() {
        val creature = Creature("Creature")
        val item = Item("Apple", weight = 1)
        creature.inventory.add(item)

        val chest = Creature("Chest", properties = Properties(Tags(listOf("Open")), Values(mapOf("Capacity" to "5"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInNonOpenContainer() {
        val creature = Creature("Creature")
        val item = Item("Apple", weight = 1)
        creature.inventory.add(item)

        val chest = Creature("Chest", properties = Properties(Tags(listOf("Container")), Values(mapOf("Capacity" to "5"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInFullContainer() {
        val creature = Creature("Creature")
        val item = Item("Apple", weight = 5)
        creature.inventory.add(item)

        val chest = Creature("Chest", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "1"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInContainerWithoutCapacity() {
        val creature = Creature("Creature")
        val item = Item("Apple", weight = 1)
        creature.inventory.add(item)

        val chest = Creature("Chest", properties = Properties(Tags(listOf("Container", "Open"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInContainerThatHasASingleMatchingTag() {
        val creature = Creature("Creature", properties = Properties(Tags(listOf("Container", "Open"))))
        val item = Item("Apple", weight = 1, properties = Properties(Tags(listOf("Food", "Fruit"))))
        creature.inventory.add(item)

        val chest = Activator("Chest", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5", "CanHold" to "Food,Apparel"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(chest.creature.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceItemInContainerThatHasNoMatchingTags() {
        val creature = Creature("Creature")
        val item = Item("Apple", weight = 1, properties = Properties(Tags(listOf("Food", "Fruit"))))
        creature.inventory.add(item)

        val chest = Creature("Chest", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "5", "CanHold" to "Weapon,Apparel"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }


}