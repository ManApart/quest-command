package use.eat

import core.GameManager
import core.commands.CommandParsers
import core.events.EventManager
import core.properties.Properties
import core.properties.Tags
import core.thing.Thing
import core.thing.item.ITEM_TAG
import core.utility.PoorMansInstrumenter
import createMockedGame
import kotlinx.coroutines.runBlocking
import traveling.location.network.NOWHERE_NODE
import use.StartUseEvent
import kotlin.test.*

class EatCommandTest {

    @BeforeTest
    fun setup() {
        createMockedGame()

    }

    @Test
    fun eatFood() {
        runBlocking {
            val player = GameManager.newPlayer(location = NOWHERE_NODE)
            val timer = PoorMansInstrumenter(10000)
            val item = Thing("Pear", properties = Properties(tags = Tags("Food", ITEM_TAG)))
            timer.printElapsed("new item")
            player.inventory.add(item)
            timer.printElapsed("add item")
            runBlocking { EatCommand().execute(player, "eat", listOf("Pear")) }
            timer.printElapsed("execute event")
            val events = EventManager.getUnexecutedEvents()
            timer.printElapsed("get events")

            assertEquals(1, events.size)
            assertTrue(events[0] is StartUseEvent)
            assertEquals(item, (events[0] as StartUseEvent).used)
            assertNull(CommandParsers.getParser(player).getResponseRequest())
        }
    }

    @Test
    fun eatMultipleFoodGivesChoice() {
        runBlocking {
            val player = GameManager.newPlayer(location = NOWHERE_NODE)
            val fruit = Thing("Pear", properties = Properties(tags = Tags("Food", ITEM_TAG)))
            val pie = Thing("Pear Pie", properties = Properties(tags = Tags("Food", ITEM_TAG)))
            player.inventory.add(fruit)
            player.inventory.add(pie)
            runBlocking { EatCommand().execute(player, "eat", listOf("Pear")) }
            val events = EventManager.getUnexecutedEvents()

            assertEquals(0, events.size)
            assertNotNull(CommandParsers.getParser(player).getResponseRequest())
        }
    }
}
