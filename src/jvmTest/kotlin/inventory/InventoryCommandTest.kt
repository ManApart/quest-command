package inventory

import core.Player
import core.events.EventManager
import core.thing.Thing
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

import kotlin.test.assertEquals


class InventoryCommandTest {

    @BeforeTest
    fun setup() {
        EventManager.clear()
    }

    @AfterTest
    fun teardown() {
        EventManager.clear()
    }

    @Test
    fun nothingToClarify() {
        val player = Player("Bob", Thing("Bob"))
        //Remove container tag so response request has no options
        player.thing.properties.tags.remove("Container")
        runBlocking { InventoryCommand().execute(player, "bag", listOf()) }

        val expected = ViewInventoryEvent(player, player.thing)
        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertEquals(expected, events.first())
    }
}