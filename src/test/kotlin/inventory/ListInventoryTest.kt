package inventory

import core.gameState.Properties
import core.gameState.Tags
import core.gameState.Target
import core.gameState.body.BodyPart
import core.history.ChatHistory
import org.junit.Test
import system.BodyFakeParser
import system.DependencyInjector
import system.body.BodyManager
import system.body.BodyParser
import kotlin.test.assertEquals

class ListInventoryTest {

    @Test
    fun listInventory() {
        val creature = Target("Chest",  properties = Properties(tags = Tags(listOf("Container"))))
        creature.inventory.add(Target("Apple"))
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Chest has:\n\tApple", ChatHistory.getLastOutput())
    }

    @Test
    fun listInventoryEquipped() {
        val chest = BodyPart("Chest", slots = listOf("Chest"))

        val bodyParser = BodyFakeParser.parserFromPart(chest)
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        val creature = Target("Soldier",  body = "body", properties = Properties(tags = Tags(listOf("Container"))))
        val item = Target("Chestplate", equipSlots = listOf(listOf("Chest")))
        creature.inventory.add(item)
        creature.body.equip(item)
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Soldier has:\n\t* Chestplate", ChatHistory.getLastOutput())
    }

    @Test
    fun listInventoryEquippedNested() {
        val item = Target("Apple")
        val pouch = Target("Pouch", equipSlots = listOf(listOf("Chest")))
        pouch.inventory.add(item)

        val chest = BodyPart("Chest", slots = listOf("Chest"))

        val bodyParser = BodyFakeParser.parserFromPart(chest)
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        val creature = Target("Soldier",  body = "body", properties = Properties(tags = Tags(listOf("Container"))))
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