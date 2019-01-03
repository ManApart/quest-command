package inventory

import core.gameState.Creature
import core.gameState.Item
import core.gameState.Properties
import core.gameState.Tags
import interact.scope.ScopeManager
import inventory.pickupItem.PickupItem
import inventory.pickupItem.PickupItemEvent
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PickupItemTest {

    @Test
    fun pickupItemFromScope() {
        ScopeManager.reset()
        
        val creature = Creature("Name", "")
        val scope = ScopeManager.getScope(creature.location)
        val item = Item("Apple")
        scope.addTarget(item)

        PickupItem().execute(PickupItemEvent(creature, item))
        assertTrue(creature.inventory.exists(item))
        assertTrue(scope.getTargets(item.name).isEmpty())

        ScopeManager.reset()
    }

    @Test
    fun pickupItemFromContainer() {
        val item = Item("Apple")
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(tags = Tags(listOf("Container", "Open"))))
        chest.inventory.add(item)

        PickupItem().execute(PickupItemEvent(creature, item, chest))

        assertTrue(creature.inventory.exists(item))
        assertFalse(chest.inventory.exists(item))
    }

    @Test
    fun doNotPickupFromNonContainer() {
        val item = Item("Apple")
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(tags = Tags(listOf("Open"))))
        chest.inventory.add(item)

        PickupItem().execute(PickupItemEvent(creature, item, chest))

        assertTrue(chest.inventory.exists(item))
        assertFalse(creature.inventory.exists(item))
    }

    @Test
    fun doNotPickupFromClosedContainer() {
        val item = Item("Apple")
        val creature = Creature("Name", "")
        val chest = Creature("Name", "", properties = Properties(tags = Tags(listOf("Container"))))
        chest.inventory.add(item)

        PickupItem().execute(PickupItemEvent(creature, item, chest))

        assertTrue(chest.inventory.exists(item))
        assertFalse(creature.inventory.exists(item))
    }


}