package inventory

import core.gameState.*
import core.history.ChatHistory
import org.junit.Test
import system.DependencyInjector
import system.ItemFakeParser
import system.ItemManager
import system.ItemParser
import kotlin.test.assertEquals

class ListInventoryTest {

    @Test
    fun listInventory() {
        val itemName = "Apple"
        val fakeParser = ItemFakeParser(listOf(Item(itemName)))
        DependencyInjector.setImplementation(ItemParser::class.java, fakeParser)
        ItemManager.reset()

        val creature = Creature("Chest", "", inventory = Inventory(listOf(itemName)), properties = Properties(tags = Tags(listOf("Container"))))
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Chest has Apple.", ChatHistory.getLastOutput())
    }

    @Test
    fun creatureWithoutTagDoesNotListInventory() {
        val itemName = "Apple"
        val fakeParser = ItemFakeParser(listOf(Item(itemName)))
        DependencyInjector.setImplementation(ItemParser::class.java, fakeParser)
        ItemManager.reset()

        val creature = Creature("Chest", "", inventory = Inventory(listOf(itemName)))
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Cannot view inventory of Chest", ChatHistory.getLastOutput())
    }



}