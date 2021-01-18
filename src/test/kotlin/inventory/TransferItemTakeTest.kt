package inventory

import core.DependencyInjector
import core.body.BodyManager
import core.properties.Properties
import core.properties.Tags
import core.target.Target
import core.target.item.ITEM_TAG
import createPouch
import inventory.putItem.TransferItem
import inventory.putItem.TransferItemEvent
import org.junit.After
import org.junit.Before
import org.junit.Test
import system.BodyFakeParser
import system.location.LocationFakeParser
import traveling.location.location.LocationManager
import traveling.location.location.LocationParser
import traveling.location.location.NOWHERE_NODE
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TransferItemTakeTest {

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
//        val pouch = Target("Pouch", body = createInventoryBody(15), properties = Properties(Tags(ITEM_TAG)))
        val pouch = createPouch(15)
        creature.inventory.add(pouch)
        return creature
    }

}