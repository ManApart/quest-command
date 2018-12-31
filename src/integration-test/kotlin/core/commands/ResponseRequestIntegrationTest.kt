package core.commands

import core.gameState.Item
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
        ScopeManager.getScope().addTarget(Item("Wheat Bundle"))
        ScopeManager.getScope().addTarget(Item("Wheat Flour"))

        val input = "pickup wheat && 2"
        CommandParser.parseCommand(input)
        assertEquals("Player picked up Wheat Flour.", ChatHistory.getLastOutput())
    }

}