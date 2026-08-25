package inventory


import core.DependencyInjector
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsMock
import core.body.*
import core.properties.Properties
import core.properties.TagStrings.ITEM
import core.properties.Tags
import core.properties.ValueStrings
import core.properties.ValueStrings.CAN_HOLD
import core.thing.Thing
import createChest
import createClosedChest
import createItem
import createPackMule
import createPouch
import inventory.putItem.TransferItem
import inventory.putItem.TransferItemEvent
import kotlinx.coroutines.runBlocking
import traveling.location.location.LocationManager
import traveling.location.location.LocationsCollection
import traveling.location.location.LocationsMock
import traveling.location.location.locations
import traveling.location.network.NOWHERE_NODE
import traveling.location.network.NetworksCollection
import traveling.location.network.NetworksMock
import traveling.location.network.networks
import kotlin.test.*

class TransferItemPlaceTest {

    @BeforeTest
    fun setup() {
        runBlocking {
            DependencyInjector.setImplementation(BodysCollection::class, BodysMock())
            BodyManager.reset()

            val behaviorParser = BehaviorsMock()
            DependencyInjector.setImplementation(BehaviorsCollection::class, behaviorParser)
            BehaviorManager.reset()

            DependencyInjector.setImplementation(NetworksCollection::class, NetworksMock())
            DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
            LocationManager.reset()

            NOWHERE_NODE.getLocation().clear()
        }
    }

    @Test
    fun placeItemInActivatorContainer() {
        runBlocking {
            val item = createItem("Apple", weight = 5)
            val creature = createChest()
            creature.add(item)

            val chest = createChest(5)

            runBlocking { TransferItem().complete(TransferItemEvent(creature, item, creature, chest)) }

            assertNotNull(chest.inventory.getItem(item.name))
            assertNull(creature.inventory.getItem(item.name))
        }
    }

    @Test
    fun placeItemInCreatureContainerSubInventory() {
        runBlocking {
            //Capacity = 10 * Strength
            val creature = createChest()
            val item = createItem("Apple", weight = 10)
            creature.add(item)

            val packMule = createPackMule(1)

            val pouch = createPouch(15)
            packMule.add(pouch)

            runBlocking { TransferItem().complete(TransferItemEvent(creature, item, creature, packMule)) }

            assertNotNull(packMule.inventory.getItem(item.name))
            assertNull(creature.inventory.getItem(item.name))
        }
    }

    @Test
    fun placeItemInCreatureContainerEquip() {
        runBlocking {
            val bodyMock = BodysMock(bodies {
                body("body") { part("Hand") }
                body("none") { part("part") }
            })

            DependencyInjector.setImplementation(BodysCollection::class, bodyMock)
            BodyManager.reset()

            val creature = createChest()
            val item = Thing("Dagger", equipTargets = listOf(EquipTarget("Grip", listOf("Hand"))), properties = Properties(tags = Tags(ITEM)))
            creature.add(item)
            val packMule = createPackMule()

            TransferItem().complete(TransferItemEvent(creature, item, creature, packMule))

            assertNotNull(packMule.inventory.getItem(item.name))
            assertNull(creature.inventory.getItem(item.name))
        }
    }

    @Test
    fun placeItemInItemContainer() {
        runBlocking {
            val creature = createChest()
            val item = createItem("Apple", weight = 5)
            creature.add(item)

            val chest = createChest(10)

            runBlocking { TransferItem().complete(TransferItemEvent(creature, item, creature, chest)) }

            assertNotNull(chest.inventory.getItem(item.name))
            assertNull(creature.inventory.getItem(item.name))
        }
    }

    @Test
    fun placeItemStackInContainer() {
        runBlocking {
            val creature = createChest()
            val item = createItem(weight = 1)
            item.properties.values.put(ValueStrings.COUNT, 2)
            creature.add(item)

            val chest = createChest(size = 5)
            TransferItem().complete(TransferItemEvent(creature, item, creature, chest))

            assertNotNull(chest.inventory.getItem(item.name))
            assertEquals(1, chest.inventory.getItem(item.name)!!.properties.getCount())

            assertNotNull(creature.inventory.getItem(item.name))
            assertEquals(1, creature.inventory.getItem(item.name)!!.properties.getCount())
        }
    }

    @Test
    fun doNotPlaceInNonContainer() {
        runBlocking {
            val creature = Thing("Creature")
            val item = createItem("Apple", 1)
            creature.add(item)

            val chest = createChest(5)

            runBlocking { TransferItem().complete(TransferItemEvent(creature, item, creature, chest)) }

            assertNotNull(creature.inventory.getItem(item.name))
            assertNull(chest.inventory.getItem(item.name))
        }
    }

    @Test
    fun doNotPlaceInNonOpenContainer() {
        runBlocking {
            val creature = Thing("Creature")
            val item = createItem("Apple", weight = 1)
            creature.add(item)

            val chest = createClosedChest(5)

            runBlocking { TransferItem().complete(TransferItemEvent(creature, item, creature, chest)) }

            assertNotNull(creature.inventory.getItem(item.name))
            assertNull(chest.inventory.getItem(item.name))
        }
    }

    @Test
    fun doNotPlaceInFullContainer() {
        runBlocking {
            val creature = Thing("Creature")
            val item = createItem("Apple", weight = 5)
            creature.add(item)

            val chest = createChest(1)

            runBlocking { TransferItem().complete(TransferItemEvent(creature, item, creature, chest)) }

            assertNotNull(creature.inventory.getItem(item.name))
            assertNull(chest.inventory.getItem(item.name))
        }
    }

    @Test
    fun doNotPlaceInContainerWithoutCapacity() {
        runBlocking {
            val creature = Thing("Creature")
            val item = createItem("Apple", weight = 1)
            creature.add(item)

            val chest = createChest(size = 0)

            runBlocking { TransferItem().complete(TransferItemEvent(creature, item, creature, chest)) }

            assertNotNull(creature.inventory.getItem(item.name))
            assertNull(chest.inventory.getItem(item.name))
        }
    }

//    @Test
//    fun chestWithNoCapacityTagAtLocation() {
//        val creature = Thing("Creature")
//        val item = createItem("Apple", weight = 1)
//        creature.add(item)
//
////        val chest = Thing("Chest", properties = Properties(Tags("Container", "Open", "Activator"))))
//
//        val chest = createChest(size = 0)
//
//        TransferItem2().execute(TransferItem2Event(creature, item, creature, chest))
//
//        assertNotNull(creature.inventory.getItem(item.name))
//        assertNull(chest.inventory.getItem(item.name))
//    }

    @Test
    fun placeItemInContainerThatHasASingleMatchingTag() {
        runBlocking {
            val creature = createChest()
            val item = createItem()
            item.properties.tags.add("Food", "Fruit")
            creature.add(item)

            val chest = createChest(5)
            chest.properties.values.put(CAN_HOLD, "Food,Apparel")

            runBlocking { TransferItem().complete(TransferItemEvent(creature, item, creature, chest)) }

            assertNotNull(chest.inventory.getItem(item.name))
            assertNull(creature.inventory.getItem(item.name))
        }
    }

    @Test
    fun doNotPlaceItemInContainerThatHasNoMatchingTags() {
        runBlocking {
            val creature = Thing("Creature")
            val item = createItem()
            item.properties.tags.add("Food", "Fruit")
            creature.add(item)

            val chest = createChest(5)
            chest.properties.values.put(CAN_HOLD, "Weapon,Apparel")

            runBlocking { TransferItem().complete(TransferItemEvent(creature, item, creature, chest)) }

            assertNotNull(creature.inventory.getItem(item.name))
            assertNull(chest.inventory.getItem(item.name))
        }
    }

}
