package inventory

import core.properties.Properties
import core.properties.Tags
import core.target.Target
import core.properties.Values
import inventory.dropItem.TransferItem
import inventory.dropItem.TransferItemEvent
import org.junit.Before
import org.junit.Test
import system.BodyFakeParser
import core.DependencyInjector
import core.body.BodyManager
import core.target.item.ITEM_TAG
import org.junit.After
import system.location.LocationFakeParser
import traveling.location.location.LocationManager
import traveling.location.location.LocationParser
import traveling.location.location.NOWHERE_NODE
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PickupItemTest {

    @Before
    fun setup() {
        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
        BodyManager.reset()

        val locationParser = LocationFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
        LocationManager.reset()

        NOWHERE_NODE.getLocation().clear()
    }
    
    @After
    fun tearDown() {
        NOWHERE_NODE.getLocation().clear()
    }

    @Test
    fun pickupItemFromLocation() {
        val creature = getCreatureWithCapacity()
        val location = creature.location.getLocation()
        val item = Target("Apple",  properties = Properties(Tags(ITEM_TAG)))
        location.addTarget(item)

        TransferItem().execute(TransferItemEvent(creature, item, destination = creature))
        assertNotNull(creature.inventory.getItem(item.name))
        assertTrue(location.getTargets(item.name).isEmpty())
    }

    @Test
    fun noPickupItemFromLocationIfNoCapacity() {
        val creature = Target("Target")
        val location = creature.location.getLocation()
        val item = Target("Apple")
        location.addTarget(item)

        TransferItem().execute(TransferItemEvent(creature, item, destination = creature))
        assertNull(creature.inventory.getItem(item.name))
        assertTrue(location.getTargets(item.name).isNotEmpty())
    }

    @Test
    fun pickupItemFromContainer() {
        val creature = getCreatureWithCapacity()

        val chest = Target("Chest", properties = Properties(tags = Tags(listOf("Container", "Open"))))
        val item = Target("Apple",  properties = Properties(Tags(ITEM_TAG)))
        chest.inventory.add(item)

        TransferItem().execute(TransferItemEvent(creature, item, chest, creature))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPickupFromNonContainer() {
        val creature = getCreatureWithCapacity()

        val chest = Target("Chest", properties = Properties(tags = Tags(listOf("Open"))))
        val item = Target("Apple",  properties = Properties(Tags(ITEM_TAG)))
        chest.inventory.add(item)

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun doNotPickupFromClosedContainer() {
        val creature = getCreatureWithCapacity()

        val chest = Target("Chest", properties = Properties(tags = Tags(listOf("Container"))))
        val item = Target("Apple",  properties = Properties(Tags(ITEM_TAG)))
        chest.inventory.add(item)

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    private fun getCreatureWithCapacity(): Target {
        val creature = Target("Target", properties = Properties(tags = Tags(listOf("Container", "Open", "Creature"))))
        val pouch = Target("Pouch", body = createInventoryBody(15), properties = Properties(Tags(ITEM_TAG)))
//        val pouch = Target("Pouch", properties = Properties(Values(mapOf("Capacity" to "15")), Tags(listOf("Container", "Open", ITEM_TAG))))
        creature.inventory.add(pouch)
        return creature
    }

}