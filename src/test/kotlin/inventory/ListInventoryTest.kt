package inventory

import core.gameState.Creature
import core.gameState.Item
import core.gameState.Properties
import core.gameState.Tags
import core.history.ChatHistory
import org.junit.Test
import kotlin.test.assertEquals

class ListInventoryTest {

    @Test
    fun listInventory() {
        val creature = Creature("Chest", "", properties = Properties(tags = Tags(listOf("Container"))))
        creature.inventory.add(Item("Apple"))
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Chest has Apple.", ChatHistory.getLastOutput())
    }

    @Test
    fun creatureWithoutTagDoesNotListInventory() {
        val creature = Creature("Chest", "")
        creature.inventory.add(Item("Apple"))
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Cannot view inventory of Chest", ChatHistory.getLastOutput())
    }


}