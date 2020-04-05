package inventory

import core.DependencyInjector
import core.body.BodyManager
import core.history.ChatHistory
import core.properties.Properties
import core.properties.Tags
import core.target.Target
import core.target.item.ITEM_TAG
import createClosedChest
import createItem
import org.junit.Test
import system.BodyFakeParser
import traveling.location.location.LocationRecipe
import traveling.location.location.LocationParser
import kotlin.test.assertEquals

class ListInventoryTest {

    @Test
    fun listInventory() {
        val creature = createClosedChest()
        creature.inventory.add(createItem())
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Closed Chest has:\n\tApple", ChatHistory.getLastOutput())
    }

    @Test
    fun listNoItemsInventory() {
        val creature = createClosedChest()
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Closed Chest has no items.", ChatHistory.getLastOutput())
    }

    @Test
    fun listInventoryEquipped() {
        val chest = LocationRecipe("Chest", slots = listOf("Chest"))

        val bodyParser = BodyFakeParser.parserFromPart(chest)
        DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
        BodyManager.reset()

        val creature = Target("Soldier", bodyName = "body", properties = Properties(tags = Tags(listOf("Container"))))
        val item = Target("Chestplate", equipSlots = listOf(listOf("Chest")), properties = Properties(tags = Tags(ITEM_TAG)))
        creature.inventory.add(item)
        creature.body.equip(item)
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Soldier has:\n\t* Chestplate", ChatHistory.getLastOutput())
    }

    @Test
    fun listInventoryEquippedNested() {
        val item = createItem("Apple")
        val pouch = Target("Pouch", equipSlots = listOf(listOf("Chest")), properties = Properties(tags = Tags(ITEM_TAG)))
        pouch.inventory.add(item)

        val chest = LocationRecipe("Chest", slots = listOf("Chest"))

        val bodyParser = BodyFakeParser.parserFromPart(chest)
        DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
        BodyManager.reset()

        val creature = Target("Soldier", bodyName = "body", properties = Properties(tags = Tags(listOf("Container"))))
        creature.inventory.add(pouch)
        creature.body.equip(pouch)

        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Soldier has:\n\t* Pouch\n\t\tApple", ChatHistory.getLastOutput())
    }

    @Test
    fun creatureWithoutTagDoesNotListInventory() {
        val creature = Target("Chest")
        creature.inventory.add(Target("Apple"))
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Cannot view inventory of Chest", ChatHistory.getLastOutput())
    }


}