package inventory

import core.Player
import core.events.EventManager
import core.thing.Thing
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class InventoryCommandTest{

    @Before
    fun setup() {
        EventManager.clear()
    }

    @After
    fun teardown() {
        EventManager.clear()
    }

    @Test
    fun nothingToClarify() {
        val player = Player("Bob", Thing("Bob"))
        //Remove container tag so response request has no options
        player.thing.properties.tags.remove("Container")
        InventoryCommand().execute(player, "bag", listOf())

        val expected = ListInventoryEvent(player, player.thing)
        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertEquals(expected, events.first())
    }
}