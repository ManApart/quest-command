package inventory

import core.gameState.Properties
import core.gameState.Tags
import core.gameState.Target
import core.gameState.Values
import interact.scope.ScopeManager
import inventory.dropItem.TransferItem
import inventory.dropItem.TransferItemEvent
import org.junit.Before
import org.junit.Test
import system.BodyFakeParser
import system.DependencyInjector
import system.body.BodyManager
import system.body.BodyParser
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PickupItemTest {

    @Before
    fun setup() {
        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()
    }

    @Test
    fun pickupItemFromScope() {
        ScopeManager.reset()

        val creature = getCreatureWithCapacity()
        val scope = ScopeManager.getScope(creature.location)
        val item = Target("Apple")
        scope.addTarget(item)

        TransferItem().execute(TransferItemEvent(creature, item, destination = creature))
        assertNotNull(creature.inventory.getItem(item.name))
        assertTrue(scope.getTargets(item.name).isEmpty())

        ScopeManager.reset()
    }

    @Test
    fun noPickupItemFromScopeIfNoCapacity() {
        ScopeManager.reset()

        val creature = Target("Target")
        val scope = ScopeManager.getScope(creature.location)
        val item = Target("Apple")
        scope.addTarget(item)

        TransferItem().execute(TransferItemEvent(creature, item, destination = creature))
        assertNull(creature.inventory.getItem(item.name))
        assertTrue(scope.getTargets(item.name).isNotEmpty())

        ScopeManager.reset()
    }

    @Test
    fun pickupItemFromContainer() {
        val creature = getCreatureWithCapacity()

        val chest = Target("Chest", properties = Properties(tags = Tags(listOf("Container", "Open"))))
        val item = Target("Apple")
        chest.inventory.add(item)

        TransferItem().execute(TransferItemEvent(creature, item, chest, creature))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPickupFromNonContainer() {
        val creature = getCreatureWithCapacity()

        val chest = Target("Chest", properties = Properties(tags = Tags(listOf("Open"))))
        val item = Target("Apple")
        chest.inventory.add(item)

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun doNotPickupFromClosedContainer() {
        val creature = getCreatureWithCapacity()

        val chest = Target("Chest", properties = Properties(tags = Tags(listOf("Container"))))
        val item = Target("Apple")
        chest.inventory.add(item)

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    private fun getCreatureWithCapacity(): Target {
        val creature = Target("Target", properties = Properties(tags = Tags(listOf("Container", "Open", "Creature"))))
        val pouch = Target("Pouch", properties = Properties(Values(mapOf("Capacity" to "15")), Tags(listOf("Container", "Open"))))
        creature.inventory.add(pouch)
        return creature
    }

}