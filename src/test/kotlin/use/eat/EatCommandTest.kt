package use.eat

import core.GameManager
import core.commands.CommandParser
import core.events.EventManager
import core.properties.Properties
import core.properties.Tags
import core.thing.Thing
import core.thing.item.ITEM_TAG
import core.utility.PoorMansInstrumenter
import createMockedGame
import org.junit.Before
import org.junit.Test
import traveling.location.network.NOWHERE_NODE
import use.StartUseEvent
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EatCommandTest {

    @Before
    fun setup() {
        createMockedGame()

    }

    @Test
    fun eatFood() {
        val player = GameManager.newPlayer(location = NOWHERE_NODE).thing
        val timer = PoorMansInstrumenter(10000)
        val item = Thing("Pear", properties = Properties(tags = Tags("Food", ITEM_TAG)))
        timer.printElapsed("new item")
        player.inventory.add(item)
        timer.printElapsed("add item")
        EatCommand().execute(player, "eat", listOf("Pear"))
        timer.printElapsed("execute event")
        val events = EventManager.getUnexecutedEvents()
        timer.printElapsed("get events")

        assertEquals(1, events.size)
        assertTrue(events[0] is StartUseEvent)
        assertEquals(item, (events[0] as StartUseEvent).used)
        assertNull(CommandParser.getResponseRequest())
    }

    @Test
    fun eatMultipleFoodGivesChoice() {
        val player = GameManager.newPlayer(location = NOWHERE_NODE).thing
        val fruit = Thing("Pear", properties = Properties(tags = Tags("Food", ITEM_TAG)))
        val pie = Thing("Pear Pie", properties = Properties(tags = Tags("Food", ITEM_TAG)))
        player.inventory.add(fruit)
        player.inventory.add(pie)
        EatCommand().execute(player, "eat", listOf("Pear"))
        val events = EventManager.getUnexecutedEvents()

        assertEquals(0, events.size)
        assertNotNull(CommandParser.getResponseRequest())
    }
}
