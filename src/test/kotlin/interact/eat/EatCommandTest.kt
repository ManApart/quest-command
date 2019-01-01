package interact.eat

import core.commands.CommandParser
import core.gameState.*
import interact.UseEvent
import org.junit.Before
import org.junit.Test
import system.EventManager
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EatCommandTest {

    @Before
    fun reset() {
        EventManager.reset()
        CommandParser.responseRequest  = null
        GameState.player = Player()
    }

    @Test
    fun eatFood() {
        val item = Item("Pear", properties = Properties(tags = Tags(listOf("Food"))))
        GameState.player.creature.inventory.add(item)
        EatCommand().execute("eat", listOf("Pear"))
        val events = EventManager.getUnexecutedEvents()

        assertEquals(1, events.size)
        assertTrue(events[0] is UseEvent)
        assertEquals(item, (events[0] as UseEvent).used)
        assertNull(CommandParser.responseRequest)
    }

    @Test
    fun eatMultipleFoodGivesChoice() {
        val fruit = Item("Pear", properties = Properties(tags = Tags(listOf("Food"))))
        val pie = Item("Pear Pie", properties = Properties(tags = Tags(listOf("Food"))))
        GameState.player.creature.inventory.add(fruit)
        GameState.player.creature.inventory.add(pie)
        EatCommand().execute("eat", listOf("Pear"))
        val events = EventManager.getUnexecutedEvents()

        assertEquals(0, events.size)
        assertNotNull(CommandParser.responseRequest)
    }
}