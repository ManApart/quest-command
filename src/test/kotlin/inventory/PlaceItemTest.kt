package inventory

import combat.battle.position.TargetPosition
import core.gameState.*
import core.gameState.body.Body
import core.gameState.body.BodyPart
import core.gameState.body.ProtoBody
import interact.scope.ScopeManager
import inventory.dropItem.TransferItem
import inventory.dropItem.TransferItemEvent
import org.junit.Test
import system.BodyFakeParser
import system.DependencyInjector
import system.body.BodyManager
import system.body.BodyParser
import kotlin.test.*

class PlaceItemTest {

    @Test
    fun dropItem() {
        val creature = Target("Creature")
        val item = Target("Apple")
        creature.inventory.add(item)
        val scope = ScopeManager.getScope(creature.location)

        TransferItem().execute(TransferItemEvent(item, creature))
        assertTrue(scope.getTargets(item.name).isNotEmpty())
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInActivatorContainer() {
        val item = createItem("Apple", weight = 5)
        val creature = createCreature()
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Tags(listOf("Container", "Open", "Activator")), Values(mapOf("Capacity" to "5"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInCreatureContainerSubInventory() {
        //Capacity = 10 * Strength
        val creature = createCreature()
        val item = createItem("Apple", weight = 10)
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Tags(listOf("Container", "Open", "Creature")), stats = Values(mapOf("Strength" to "1"))))
        val pouch = Target("Pouch", properties = Properties(Tags(listOf("Container", "Open")), Values(mapOf("Capacity" to "15"))))
        chest.inventory.add(pouch)

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInCreatureContainerEquip() {
        val creature = createCreature()
        val item = Target("Dagger", equipSlots = listOf(listOf("Grip")))
        creature.inventory.add(item)

        val part = BodyPart("Hand", TargetPosition(), listOf("Grip", "Glove"))

        val bodyParser = BodyFakeParser(listOf(ProtoBody("body", listOf("Hand"))), listOf(part))
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        val chest = Target("Chest", body = "body", properties = Properties(Tags(listOf("Container", "Open", "Creature")), stats = Values(mapOf("Strength" to "1"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInItemContainer() {
        val creature = createCreature()
        val item = createItem("Apple", weight = 5)
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Tags(listOf("Container", "Open", "Activator")), Values(mapOf("Capacity" to "5"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun placeItemStackInContainer() {
        val creature = createCreature()
        val item = Target("Apple", properties = Properties(stats = Values(mapOf("weight" to "5", "count" to "2"))))
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Tags(listOf("Container", "Open", "Activator")), Values(mapOf("Capacity" to "5"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

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

        val chest = Target("Chest", properties = Properties(Tags(listOf("Open", "Activator")), Values(mapOf("Capacity" to "5"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInNonOpenContainer() {
        val creature = Target("Creature")
        val item = createItem("Apple", weight = 1)
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Tags(listOf("Container", "Activator")), Values(mapOf("Capacity" to "5"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInFullContainer() {
        val creature = Target("Creature")
        val item = createItem("Apple", weight = 5)
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Tags(listOf("Container", "Open", "Activator")), Values(mapOf("Capacity" to "1"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceInContainerWithoutCapacity() {
        val creature = Target("Creature")
        val item = createItem("Apple", weight = 1)
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Tags(listOf("Container", "Open", "Activator"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    @Test
    fun placeItemInContainerThatHasASingleMatchingTag() {
        val creature = createCreature()
        val item = Target("Apple", properties = Properties(stats = Values(mapOf("weight" to "1")), tags = Tags(listOf("Food", "Fruit"))))
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Tags(listOf("Container", "Open", "Activator")), Values(mapOf("Capacity" to "5", "CanHold" to "Food,Apparel"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(chest.inventory.getItem(item.name))
        assertNull(creature.inventory.getItem(item.name))
    }

    @Test
    fun doNotPlaceItemInContainerThatHasNoMatchingTags() {
        val creature = Target("Creature")
        val item = Target("Apple", properties = Properties(stats = Values(mapOf("weight" to "1")), tags = Tags(listOf("Food", "Fruit"))))
        creature.inventory.add(item)

        val chest = Target("Chest", properties = Properties(Tags(listOf("Container", "Open", "Activator")), Values(mapOf("Capacity" to "5", "CanHold" to "Weapon,Apparel"))))

        TransferItem().execute(TransferItemEvent(item, creature, chest))

        assertNotNull(creature.inventory.getItem(item.name))
        assertNull(chest.inventory.getItem(item.name))
    }

    private fun createItem(name: String, weight: Int): core.gameState.Target {
        return Target(name, properties = Properties(stats = Values(mapOf("weight" to weight.toString()))))
    }

    private fun createCreature() = Target("Creature", properties = Properties(Tags(listOf("Container", "Open", "Creature"))))
}