package inventory

import core.DependencyInjector
import core.body.*
import core.properties.Properties
import core.properties.TagStrings.CONTAINER
import core.properties.TagStrings.CREATURE
import core.properties.TagStrings.ITEM
import core.properties.TagStrings.OPEN
import core.properties.Tags
import core.properties.ValueStrings.CAPACITY
import core.properties.ValueStrings.COUNT
import core.properties.Values
import core.properties.values
import core.thing.Thing
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
            val location = creature.location.getLocation()
            val item = Thing("Apple", properties = Properties(Tags(ITEM)))
            location.addThing(item)

            take(creature, item)
            assertNotNull(creature.inventory.getItem(item.name))
            assertTrue(location.getThings(item.name).isEmpty())
        }
    }

    @Test
    fun noPickupItemFromLocationIfNoCapacity() {
        runBlocking {
            val creature = Thing("Thing")
            val location = creature.location.getLocation()
            val item = Thing("Apple")
            location.addThing(item)

            take(creature, item)
            assertNull(creature.inventory.getItem(item.name))
            assertTrue(location.getThings(item.name).isNotEmpty())
        }
    }

    @Test
    fun pickupSingleItemLeavesRestOfStack() {
        runBlocking {
            val creature = getCreatureWithCapacity()
            val location = creature.location.getLocation()
            val item = Thing("Apple", properties = Properties(Values(COUNT to "3"), Tags(ITEM)))
            location.addThing(item)

            take(creature, item)
            val inInventory = creature.inventory.getItem(item.name)
            val inLocation = location.getItems(item.name).firstOrNull()

            assertNotNull(inInventory)
            assertNotNull(inLocation)

            assertEquals(1, inInventory.properties.values.getInt(COUNT))
            assertEquals(2, inLocation.properties.values.getInt(COUNT))
            assertEquals(location, inLocation.location.getLocation())
            assertEquals(NOWHERE_NODE, inInventory.location)
        }
    }

    @Test
    fun takeItemIntoPouchDirectly() {
        runBlocking {
            val creature = getCreatureWithCapacity()

            val location = creature.location.getLocation()
            val item = Thing("Apple")
            location.addThing(item)

            val pouch = createPouch(1)
            creature.add(pouch)

            TakeItem().complete(TakeItemEvent(creature, item, pouch))
            assertFalse(creature.inventory.getItems().contains(item))
            assertNotNull(pouch.inventory.getItem(item.name))
            assertTrue(location.getThings(item.name).isEmpty())
        }
    }

    @Test
    fun addItemToInventoryAndThenContainer() {
        runBlocking {
            val creature = getCreatureWithCapacity(1)
            val location = creature.location.getLocation()
            val item = Thing("Apple")
            location.addThing(item)

            //First Apple goes to inventory
            take(creature, item)
            val takenApple = creature.inventory.getItem(item.name)
            assertNotNull(takenApple)
            assertTrue(location.getThings(item.name).isEmpty())

            val item2 = Thing("Apple")
            location.addThing(item)

            //Second item can't fit in player
            take(creature, item2)
            assertNotNull(takenApple)
            assertEquals(1, takenApple.properties.getCount())
            assertTrue(location.getThings(item.name).isNotEmpty())

            val pouch = createPouch(1)
            creature.add(pouch)

            //Second item fits in pouch
            take(creature, item2)
            val pouchApple = pouch.inventory.getItem(item.name)
            assertNotNull(takenApple)
            assertEquals(1, takenApple.properties.getCount())
            assertNotNull(pouchApple)
            assertEquals(1, pouchApple.properties.getCount())
            assertTrue(location.getThings(item.name).isEmpty())

        }
    }

    private fun getCreatureWithCapacity(capacity: Int = 15): Thing {
        return Thing("Thing", properties = Properties(tags = Tags(OPEN, CONTAINER, CREATURE), values = values(CAPACITY to capacity)))
    }

    private suspend fun take(creature: Thing, item: Thing) {
        TakeItem().complete(TakeItemEvent(creature, item, creature))
    }

}
