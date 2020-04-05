package inventory

import core.DependencyInjector
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorParser
import core.body.BodyManager
import core.properties.Properties
import core.properties.Tags
import core.properties.Values
import core.target.Target
import core.target.item.ITEM_TAG
import createChest
import createItem
import inventory.dropItem.TransferItem
import inventory.dropItem.TransferItemEvent
import org.junit.Before
import org.junit.Test
import system.BehaviorFakeParser
import system.BodyFakeParser
import system.location.LocationFakeParser
import traveling.location.location.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PlaceItemTest {

    @Before
    fun setup() {
        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
        BodyManager.reset()

        val behaviorParser = BehaviorFakeParser()
        DependencyInjector.setImplementation(BehaviorParser::class.java, behaviorParser)
        BehaviorManager.reset()

        val locationParser = LocationFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
        LocationManager.reset()

        NOWHERE_NODE.getLocation().clear()
    }

    @Test
    fun dropItem() {
        val creature = Target("Creature")
        val item = Target("Apple")
        creature.inventory.add(item)
        val scope = creature.location.getLocation()

        TransferItem().execute(TransferItemEvent(creature, item, creature))
        assertTrue(scope.getTargets(item.name).isNotEmpty())
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInActivatorContainer() {
        val item = createItem("Apple", weight = 5)
        val creature = createChest()
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Size" to "5")), Tags(listOf("Container", "Open", "Activator"))))

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

        val chest = Target("Chest", properties = Properties(Values(mapOf("Strength" to "1")), Tags(listOf("Container", "Open", "Creature"))))
        val pouch = Target("Pouch", properties = Properties(Values(mapOf("Size" to "15")), Tags(listOf("Container", "Open"))))
        chest.inventory.add(pouch)

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
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


        val chest = Target("Chest", bodyName = "body", properties = Properties(Values(mapOf("Strength" to "1")), Tags(listOf("Container", "Open", "Creature"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInItemContainer() {
        val creature = createChest()
        val item = createItem("Apple", weight = 5)
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Size" to "5")), Tags(listOf("Container", "Open", "Activator"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemStackInContainer() {
        val creature = createChest()
        val item = Target("Apple", properties = Properties(Values(mapOf("weight" to "5", "count" to "2")), Tags(ITEM_TAG)))
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

        val chest = Target("Chest", properties = Properties(Values(mapOf("Size" to "5")), Tags(listOf("Open", "Activator"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInNonOpenContainer() {
        val creature = Target("Creature")
        val item = createItem("Apple", weight = 1)
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Size" to "5")), Tags(listOf("Container", "Activator"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInFullContainer() {
        val creature = Target("Creature")
        val item = createItem("Apple", weight = 5)
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Size" to "1")), Tags(listOf("Container", "Open", "Activator"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInContainerWithoutCapacity() {
        val creature = Target("Creature")
        val item = createItem("Apple", weight = 1)
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Tags(listOf("Container", "Open", "Activator"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInContainerThatHasASingleMatchingTag() {
        val creature = createChest()
        val item = Target("Apple", properties = Properties(Values(mapOf("weight" to "1")), Tags(listOf("Food", "Fruit", ITEM_TAG))))
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Size" to "5", "CanHold" to "Food,Apparel")), Tags(listOf("Container", "Open", "Activator"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceItemInContainerThatHasNoMatchingTags() {
        val creature = Target("Creature")
        val item = Target("Apple", properties = Properties(Values(mapOf("weight" to "1")), Tags(listOf("Food", "Fruit", ITEM_TAG))))
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Size" to "5", "CanHold" to "Weapon,Apparel")), Tags(listOf("Container", "Open", "Activator"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

}