package inventory

import core.gameState.*
import interact.scope.ScopeManager
import inventory.dropItem.TransferItem
import inventory.dropItem.TransferItemEvent
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PickupItemTest {

    @Test
    fun pickupItemFromScope() {
        ScopeManager.reset()

        val creature = getCreatureWithCapacity()
        val scope = ScopeManager.getScope(creature.location)
        val item = Item("Apple")
        scope.addTarget(item)

        TransferItem().execute(TransferItemEvent(item, destination = creature))
        assertNotNull(creature.inventory.getItem(item.name))
        assertTrue(scope.getTargets(item.name).isEmpty())

        ScopeManager.reset()
    }

    @Test
    fun noPickupItemFromScopeIfNoCapacity() {
        ScopeManager.reset()

        val creature = Creature("Creature")
        val scope = ScopeManager.getScope(creature.location)
        val item = Item("Apple")
        scope.addTarget(item)

        TransferItem().execute(TransferItemEvent(item, destination = creature))
        assertNull(creature.inventory.getItem(item.name))
        assertTrue(scope.getTargets(item.name).isNotEmpty())

        ScopeManager.reset()
    }

    @Test
    fun pickupItemFromContainer() {
        val creature = getCreatureWithCapacity()

        val chest = Creature("Chest", properties = Properties(tags = Tags(listOf("Container", "Open"))))
        val item = Item("Apple")
        chest.inventory.add(item)

        TransferItem().execute(TransferItemEvent(item, chest, creature))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPickupFromNonContainer() {
        val creature = getCreatureWithCapacity()

        val chest = Creature("Chest", properties = Properties(tags = Tags(listOf("Open"))))
        val item = Item("Apple")
        chest.inventory.add(item)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun doNotPickupFromClosedContainer() {
        val creature = getCreatureWithCapacity()

        val chest = Creature("Chest", properties = Properties(tags = Tags(listOf("Container"))))
        val item = Item("Apple")
        chest.inventory.add(item)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    private fun getCreatureWithCapacity(): Creature {
        val creature = Creature("Creature", properties = Properties(Tags(listOf("Container", "Open"))))
        val pouch = Item("Pouch", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "15"))))
        creature.inventory.add(pouch)
        return creature
    }

}