package core.commands

import core.GameManager
import core.GameState
import core.events.EventManager
import core.history.GameLogger
import core.properties.Tags
import core.thing.Thing
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ResponseRequestIntegrationTest {

    @Before
    fun reset() {
        GameManager.newGame(testing = true)
        EventManager.executeEvents()
    }

    @Test
    fun takeSecondObject() {
        val props = core.properties.Properties(tags = Tags("Item"))
        GameState.player.thing.currentLocation().addThing(Thing("Wheat Bundle", properties = props))
        GameState.player.thing.currentLocation().addThing(Thing("Wheat Flour", properties = props))

        val input = "pickup wheat && 2"
        CommandParsers.parseCommand(GameState.player, input)
        assertEquals("You picked up Wheat Flour.", GameLogger.getMainHistory().getLastOutput())
    }

}