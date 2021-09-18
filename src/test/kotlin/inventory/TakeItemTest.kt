package inventory

import core.DependencyInjector
import core.body.*
import core.properties.*
import core.target.Target
import core.target.item.ITEM_TAG
import createPouch
import inventory.pickupItem.TakeItem
import inventory.pickupItem.TakeItemEvent
import org.junit.After
import org.junit.Before
import org.junit.Test
import traveling.location.location.*
import traveling.location.network.NOWHERE_NODE
import traveling.location.network.NetworksCollection
import traveling.location.network.NetworksMock
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TakeItemTest {

    @Before
    fun setup() {
        DependencyInjector.setImplementation(BodysCollection::class, BodysMock())
        DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock())
        BodyManager.reset()

        DependencyInjector.setImplementation(NetworksCollection::class, NetworksMock())
        DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
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

        TakeItem().execute(TakeItemEvent(creature, item))
        assertNotNull(creature.inventory.getItem(item.name))
        assertTrue(location.getTargets(item.name).isEmpty())
    }

    @Test
    fun noPickupItemFromLocationIfNoCapacity() {
        val creature = Target("Target")
        val location = creature.location.getLocation()
        val item = Target("Apple")
        location.addTarget(item)

        TakeItem().execute(TakeItemEvent(creature, item))
        assertNull(creature.inventory.getItem(item.name))
        assertTrue(location.getTargets(item.name).isNotEmpty())
    }

    @Test
    fun pickupSingleItemLeavesRestOfStack() {
        val creature = getCreatureWithCapacity()
        val location = creature.location.getLocation()
        val item = Target("Apple",  properties = Properties(Values(mapOf(COUNT to "3")), Tags(ITEM_TAG)))
        location.addTarget(item)

        TakeItem().execute(TakeItemEvent(creature, item))
        val inInventory = creature.inventory.getItem(item.name)
        val inLocation = location.getItems(item.name).firstOrNull()

        assertNotNull(inInventory)
        assertNotNull(inLocation)

        assertEquals(1, inInventory.properties.values.getInt(COUNT))
        assertEquals(2, inLocation.properties.values.getInt(COUNT))

        assertEquals(location, inLocation.location.getLocation())
        assertEquals(creature.inventory.getItem("Pouch")?.body?.getRootPart(), inInventory.location.getLocation())
    }

    private fun getCreatureWithCapacity(): Target {
        val creature = Target("Target", properties = Properties(tags = Tags("Container", "Open", "Creature")))
//        val pouch = Target("Pouch", body = createInventoryBody(15), properties = Properties(Tags(ITEM_TAG)))
        val pouch = createPouch(15)
        creature.inventory.add(pouch)
        return creature
    }

}