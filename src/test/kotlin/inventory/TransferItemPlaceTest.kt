package inventory

import core.DependencyInjector
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsMock
import core.body.BodyManager
import core.properties.CAN_HOLD
import core.properties.COUNT
import core.properties.Properties
import core.properties.Tags
import core.target.Target
import core.target.item.ITEM_TAG
import createChest
import createClosedChest
import createItem
import createPackMule
import createPouch
import inventory.putItem.TransferItem
import inventory.putItem.TransferItemEvent
import org.junit.Before
import org.junit.Test
import system.BodyFakeParser
import system.location.LocationFakeParser
import traveling.location.location.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TransferItemPlaceTest {

    @Before
    fun setup() {
        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
        BodyManager.reset()

        val behaviorParser = BehaviorsMock()
        DependencyInjector.setImplementation(BehaviorsCollection::class.java, behaviorParser)
        BehaviorManager.reset()

        val locationParser = LocationFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
        LocationManager.reset()

        NOWHERE_NODE.getLocation().clear()
    }

    @Test
    fun placeItemInActivatorContainer() {
        val item = createItem("Apple", weight = 5)
        val creature = createChest()
        creature.inventory.add(item)

        val chest = createChest(5)

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInCreatureContainerSubInventory() {
        //Capacity = 10 * Strength
        val creature = createChest()
        val item = createItem("Apple", weight = 10)
        creature.inventory.add(item)

        val packMule = createPackMule(1)

        val pouch = createPouch(15)
        packMule.inventory.add(pouch)

        TransferItem().execute(TransferItemEvent(creature, item, creature, packMule))

        assertNotNull(packMule.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInCreatureContainerEquip() {
        val hand = LocationRecipe("Hand", slots = listOf("Grip", "Glove"))
        val part = LocationRecipe("part")

        val bodyParser = BodyFakeParser(
                listOf(LocationNode(name = "Hand", parent = "body"), LocationNode(name = "part", parent = "none")),
                listOf(hand, part))
        DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
        BodyManager.reset()

        val creature = createChest()
        val item = Target("Dagger", equipSlots = listOf(listOf("Grip")), properties = Properties(tags = Tags(ITEM_TAG)))
        creature.inventory.add(item)


        val packMule = createPackMule(1)

        TransferItem().execute(TransferItemEvent(creature, item, creature, packMule))

        assertNotNull(packMule.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInItemContainer() {
        val creature = createChest()
        val item = createItem("Apple", weight = 5)
        creature.inventory.add(item)

        val chest = createChest(10)

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemStackInContainer() {
        val creature = createChest()
        val item = createItem(weight = 1)
        item.properties.values.put(COUNT, 2)
        creature.inventory.add(item)

        val chest = createChest(size = 5)
        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertEquals(1, chest.inventory.getItem(item.name)!!.properties.getCount())

        assertNotNull(creature.inventory.getItem(item.name))
        assertEquals(1, creature.inventory.getItem(item.name)!!.properties.getCount())
    }

    @Test
    fun doNotPlaceInNonContainer() {
        val creature = Target("Creature")
        val item = createItem("Apple", 1)
        creature.inventory.add(item)

        val chest = createChest(5)

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInNonOpenContainer() {
        val creature = Target("Creature")
        val item = createItem("Apple", weight = 1)
        creature.inventory.add(item)

        val chest = createClosedChest(5)

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInFullContainer() {
        val creature = Target("Creature")
        val item = createItem("Apple", weight = 5)
        creature.inventory.add(item)

        val chest = createChest(1)

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInContainerWithoutCapacity() {
        val creature = Target("Creature")
        val item = createItem("Apple", weight = 1)
        creature.inventory.add(item)

//        val chest = Target("Chest", properties = Properties(Tags(listOf("Container", "Open", "Activator"))))

        val chest = createChest(size = 0)

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

//    @Test
//    fun chestWithNoCapacityTagAtLocation() {
//        val creature = Target("Creature")
//        val item = createItem("Apple", weight = 1)
//        creature.inventory.add(item)
//
////        val chest = Target("Chest", properties = Properties(Tags(listOf("Container", "Open", "Activator"))))
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
        val creature = createChest()
        val item = createItem()
        item.properties.tags.add("Food", "Fruit")
        creature.inventory.add(item)

        val chest = createChest(5)
        chest.body.getRootPart().properties.values.put(CAN_HOLD, "Food,Apparel")

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceItemInContainerThatHasNoMatchingTags() {
        val creature = Target("Creature")
        val item = createItem()
        item.properties.tags.add("Food", "Fruit")
        creature.inventory.add(item)

        val chest = createChest(5)
        chest.body.getRootPart().properties.values.put(CAN_HOLD, "Weapon,Apparel")

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

}