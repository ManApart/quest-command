package system.connection

import core.GameManager
import core.commands.CommandParsers
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
import use.eat.EatCommand
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ConnectionCommandTest {
//
//    @Before
//    fun setup() {
//        createMockedGame()
//
//    }
//
//    @Test
//    fun eatFood() {
//        val player = GameManager.newPlayer(location = NOWHERE_NODE)
//        val timer = PoorMansInstrumenter(10000)
//        val item = Thing("Pear", properties = Properties(tags = Tags("Food", ITEM_TAG)))
//        timer.printElapsed("new item")
//        player.inventory.add(item)
//        timer.printElapsed("add item")
//        EatCommand().execute(player, "eat", listOf("Pear"))
//        timer.printElapsed("execute event")
//        val events = EventManager.getUnexecutedEvents()
//        timer.printElapsed("get events")
//
//        assertEquals(1, events.size)
//        assertTrue(events[0] is StartUseEvent)
//        assertEquals(item, (events[0] as StartUseEvent).used)
//        assertNull(CommandParsers.getParser(player).getResponseRequest())
//    }
}
