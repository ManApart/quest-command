package core.commands

import core.GameManager
import core.GameState
import core.events.EventManager
import core.history.ChatHistory
import core.properties.Properties
import core.properties.Tags
import core.target.Target
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals

class ResponseRequestIntegrationTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            EventManager.registerListeners()
        }
    }

    @Before
    fun reset() {
        GameManager.newGame(testing = true)
        EventManager.executeEvents()
    }

    @Test
    fun takeSecondObject() {
        val props = Properties(tags = Tags("Item"))
        GameState.player.currentLocation().addTarget(Target("Wheat Bundle", properties = props))
        GameState.player.currentLocation().addTarget(Target("Wheat Flour", properties = props))

        val input = "pickup wheat && 2"
        CommandParser.parseCommand(input)
        assertEquals("Player picked up Wheat Flour.", ChatHistory.getLastOutput())
    }

}