package inventory

import core.properties.Properties
import core.properties.Tags
import core.target.Target
import core.properties.Values
import core.body.BodyPart
import traveling.location.LocationNode
import traveling.scope.ScopeManager
import inventory.dropItem.TransferItem
import inventory.dropItem.TransferItemEvent
import org.junit.Before
import org.junit.Test
import system.BehaviorFakeParser
import system.BodyFakeParser
import core.DependencyInjector
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorParser
import core.body.BodyManager
import core.body.BodyParser
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PlaceItemTest {


    @Before
    fun setup() {
        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        val behaviorParser = BehaviorFakeParser()
        DependencyInjector.setImplementation(BehaviorParser::class.java, behaviorParser)
        BehaviorManager.reset()

    }

    @Test
    fun dropItem() {
        val creature = Target("Creature")
        val item = Target("Apple")
        creature.inventory.add(item)
        val scope = ScopeManager.getScope(creature.location)

        TransferItem().execute(TransferItemEvent(creature, item, creature))
        assertTrue(scope.getTargets(item.name).isNotEmpty())
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInActivatorContainer() {
        val item = createItem("Apple", weight = 5)
        val creature = createCreature()
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Capacity" to "5")), Tags(listOf("Container", "Open", "Activator"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInCreatureContainerSubInventory() {
        //Capacity = 10 * Strength
        val creature = createCreature()
        val item = createItem("Apple", weight = 10)
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Strength" to "1")), Tags(listOf("Container", "Open", "Creature"))))
        val pouch = Target("Pouch", properties = Properties(Values(mapOf("Capacity" to "15")), Tags(listOf("Container", "Open"))))
        chest.inventory.add(pouch)

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInCreatureContainerEquip() {
        val hand = BodyPart("Hand", listOf("Grip", "Glove"))
        val part = BodyPart("part")

        val bodyParser = BodyFakeParser(
                listOf(LocationNode(parent = "body", name = "Hand"), LocationNode(parent = "none", name = "part")),
                listOf(hand, part))
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        val creature = createCreature()
        val item = Target("Dagger", equipSlots = listOf(listOf("Grip")))
        creature.inventory.add(item)


        val chest = Target("Chest", body = "body", properties = Properties(Values(mapOf("Strength" to "1")), Tags(listOf("Container", "Open", "Creature"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInItemContainer() {
        val creature = createCreature()
        val item = createItem("Apple", weight = 5)
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Capacity" to "5")), Tags(listOf("Container", "Open", "Activator"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemStackInContainer() {
        val creature = createCreature()
        val item = Target("Apple", properties = Properties(Values(mapOf("weight" to "5", "count" to "2"))))
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Capacity" to "5")), Tags(listOf("Container", "Open", "Activator"))))

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

        val chest = Target("Chest", properties = Properties(Values(mapOf("Capacity" to "5")), Tags(listOf("Open", "Activator"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInNonOpenContainer() {
        val creature = Target("Creature")
        val item = createItem("Apple", weight = 1)
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Capacity" to "5")), Tags(listOf("Container", "Activator"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInFullContainer() {
        val creature = Target("Creature")
        val item = createItem("Apple", weight = 5)
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Capacity" to "1")), Tags(listOf("Container", "Open", "Activator"))))

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
        val creature = createCreature()
        val item = Target("Apple", properties = Properties(Values(mapOf("weight" to "1")), Tags(listOf("Food", "Fruit"))))
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Capacity" to "5", "CanHold" to "Food,Apparel")), Tags(listOf("Container", "Open", "Activator"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceItemInContainerThatHasNoMatchingTags() {
        val creature = Target("Creature")
        val item = Target("Apple", properties = Properties(Values(mapOf("weight" to "1")), Tags(listOf("Food", "Fruit"))))
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Values(mapOf("Capacity" to "5", "CanHold" to "Weapon,Apparel")), Tags(listOf("Container", "Open", "Activator"))))

        TransferItem().execute(TransferItemEvent(creature, item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    private fun createItem(name: String, weight: Int): Target {
        return Target(name, properties = Properties(Values(mapOf("weight" to weight.toString()))))
    }

    private fun createCreature() = Target("Creature", properties = Properties(Tags(listOf("Container", "Open", "Creature"))))
}