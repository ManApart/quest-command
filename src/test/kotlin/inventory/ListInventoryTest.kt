package inventory

import core.gameState.*
import core.gameState.body.Body
import core.gameState.body.BodyPart
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
        assertEquals("Chest has:\n\tApple", ChatHistory.getLastOutput())
    }

    @Test
    fun listInventoryEquipped() {
        val creature = Creature("Soldier", "", body = Body(parts = listOf(BodyPart("Chest", slots = listOf("Chest")))), properties = Properties(tags = Tags(listOf("Container"))))
        val item = Item("Chestplate", equipSlots = listOf(listOf("Chest")))
        creature.inventory.add(item)
        creature.body.equip(item)
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Soldier has:\n\t* Chestplate", ChatHistory.getLastOutput())
    }

    @Test
    fun listInventoryEquippedNested() {
        val item = Item("Apple")
        val pouch = Item("Pouch", equipSlots = listOf(listOf("Chest")))
        pouch.inventory.add(item)

        val creature = Creature("Soldier", "", body = Body(parts = listOf(BodyPart("Chest", slots = listOf("Chest")))), properties = Properties(tags = Tags(listOf("Container"))))
        creature.inventory.add(pouch)
        creature.body.equip(pouch)

        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Soldier has:\n\t* Pouch\n\t\tApple", ChatHistory.getLastOutput())
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