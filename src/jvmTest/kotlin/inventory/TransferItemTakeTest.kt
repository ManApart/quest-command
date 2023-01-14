package inventory

import core.DependencyInjector
import core.body.*
import core.properties.Properties
import core.properties.Tags
import core.thing.Thing
import core.thing.item.ITEM_TAG
import createPouch
import inventory.putItem.TransferItem
import inventory.putItem.TransferItemEvent
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import traveling.location.location.LocationManager
import traveling.location.location.LocationsCollection
import traveling.location.location.LocationsMock
import traveling.location.network.NOWHERE_NODE
import traveling.location.network.NetworksCollection
import traveling.location.network.NetworksMock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TransferItemTakeTest {

    @BeforeTest
    fun setup() {
        DependencyInjector.setImplementation(BodysCollection::class, BodysMock())
        DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock())
        BodyManager.reset()

        DependencyInjector.setImplementation(NetworksCollection::class, NetworksMock())
        DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
        LocationManager.reset()

        NOWHERE_NODE.getLocation().clear()
    }

    @AfterTest
    fun tearDown() {
        NOWHERE_NODE.getLocation().clear()
    }

    @Test
    fun pickupItemFromContainer() {
        val creature = getCreatureWithCapacity()

        val chest = Thing("Chest", properties = Properties(tags = Tags("Container", "Open")))
        val item = Thing("Apple", properties = Properties(Tags(ITEM_TAG)))
        chest.inventory.add(item)

        runBlocking { TransferItem().execute(TransferItemEvent(creature, item, chest, creature)) }

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPickupFromNonContainer() {
        val creature = getCreatureWithCapacity()

        val chest = Thing("Chest", properties = Properties(tags = Tags("Open")))
        val item = Thing("Apple", properties = Properties(Tags(ITEM_TAG)))
        chest.inventory.add(item)

        runBlocking { TransferItem().execute(TransferItemEvent(creature, item, creature, chest)) }

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun doNotPickupFromClosedContainer() {
        val creature = getCreatureWithCapacity()

        val chest = Thing("Chest", properties = Properties(tags = Tags("Container")))
        val item = Thing("Apple", properties = Properties(Tags(ITEM_TAG)))
        chest.inventory.add(item)

        runBlocking { TransferItem().execute(TransferItemEvent(creature, item, creature, chest)) }

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    private fun getCreatureWithCapacity(): Thing {
        val creature = Thing("Thing", properties = Properties(tags = Tags("Container", "Open", "Creature")))
//        val pouch = Thing("Pouch", body = createInventoryBody(15), properties = Properties(Tags(ITEM_TAG)))
        val pouch = createPouch(15)
        creature.inventory.add(pouch)
        return creature
    }

}