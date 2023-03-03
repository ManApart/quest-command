package inventory

import core.DependencyInjector
import core.body.*
import core.properties.COUNT
import core.properties.Properties
import core.properties.Tags
import core.properties.Values
import core.thing.Thing
import core.thing.item.ITEM_TAG
import createPouch
import inventory.pickupItem.TakeItem
import inventory.pickupItem.TakeItemEvent
import kotlinx.coroutines.runBlocking
import traveling.location.location.LocationManager
import traveling.location.location.LocationsCollection
import traveling.location.location.LocationsMock
import traveling.location.network.NOWHERE_NODE
import traveling.location.network.NetworksCollection
import traveling.location.network.NetworksMock
import kotlin.test.*

class TakeItemTest {

    @BeforeTest
    fun setup() {
        DependencyInjector.setImplementation(BodysCollection::class, BodysMock())
        DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock())
        BodyManager.reset()

        DependencyInjector.setImplementation(NetworksCollection::class, NetworksMock())
        DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
        LocationManager.reset()

        runBlocking { NOWHERE_NODE.getLocation().clear() }
    }

    @AfterTest
    fun tearDown() {
        runBlocking { NOWHERE_NODE.getLocation().clear() }
    }

    @Test
    fun pickupItemFromLocation() {
        runBlocking {
            val creature = getCreatureWithCapacity()
            val location = runBlocking { creature.location.getLocation() }
            val item = Thing("Apple", properties = Properties(Tags(ITEM_TAG)))
            location.addThing(item)

            TakeItem().complete(TakeItemEvent(creature, item))
            assertNotNull(creature.inventory.getItem(item.name))
            assertTrue(location.getThings(item.name).isEmpty())
        }
    }

    @Test
    fun noPickupItemFromLocationIfNoCapacity() {
        runBlocking {
            val creature = Thing("Thing")
            val location = runBlocking { creature.location.getLocation() }
            val item = Thing("Apple")
            location.addThing(item)

            TakeItem().complete(TakeItemEvent(creature, item))
            assertNull(creature.inventory.getItem(item.name))
            assertTrue(location.getThings(item.name).isNotEmpty())
        }
    }

    @Test
    fun pickupSingleItemLeavesRestOfStack() {
        runBlocking {
            val creature = getCreatureWithCapacity()
            val location = runBlocking { creature.location.getLocation() }
            val item = Thing("Apple", properties = Properties(Values(COUNT to "3"), Tags(ITEM_TAG)))
            location.addThing(item)

            TakeItem().complete(TakeItemEvent(creature, item))
            val inInventory = creature.inventory.getItem(item.name)
            val inLocation = location.getItems(item.name).firstOrNull()

            assertNotNull(inInventory)
            assertNotNull(inLocation)

            assertEquals(1, inInventory.properties.values.getInt(COUNT))
            assertEquals(2, inLocation.properties.values.getInt(COUNT))
            assertEquals(location, inLocation.location.getLocation())
            assertEquals(creature.inventory.getItem("Pouch")?.body?.getRootPart(), inInventory.location.getLocation())
        }
    }

    private suspend fun getCreatureWithCapacity(): Thing {
        val creature = Thing("Thing", properties = Properties(tags = Tags("Container", "Open", "Creature")))
//        val pouch = Thing("Pouch", body = createInventoryBody(15), properties = Properties(Tags(ITEM_TAG)))
        val pouch = createPouch(15)
        creature.inventory.add(pouch)
        return creature
    }

}