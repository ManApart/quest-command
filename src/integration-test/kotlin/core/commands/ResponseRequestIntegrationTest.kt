package core.commands

import core.properties.Properties
import core.properties.Tags
import core.target.Target
import core.history.ChatHistory
import traveling.scope.ScopeManager
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import core.events.EventManager
import core.GameManager
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
        GameManager.newGame()
        EventManager.executeEvents()
    }

    @Test
    fun takeSecondObject() {
        val props = Properties(tags = Tags(listOf("Item")))
        ScopeManager.getScope().addTarget(Target("Wheat Bundle", properties = props))
        ScopeManager.getScope().addTarget(Target("Wheat Flour", properties = props))

        val input = "pickup wheat && 2"
        CommandParser.parseCommand(input)
        assertEquals("Player picked up Wheat Flour.", ChatHistory.getLastOutput())
    }

}