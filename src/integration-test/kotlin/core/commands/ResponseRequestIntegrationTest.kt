package core.commands

import core.gameState.Properties
import core.gameState.Tags
import core.gameState.Target
import core.history.ChatHistory
import interact.scope.ScopeManager
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import system.EventManager
import system.GameManager
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