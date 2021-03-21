package commandCombos

import core.GameManager
import core.commands.CommandParser
import core.events.EventManager
import core.history.ChatHistory
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ConversationsTest {

    @Before
    fun reset() {
        EventManager.clear()
        EventManager.registerListeners()
        GameManager.newGame(testing = true)
        EventManager.executeEvents()
    }

    @Test
    fun talkWithFarmer() {
        CommandParser.parseCommand("w && speak with farmer")
        CommandParser.parseCommand("where are you?")
        assertEquals("Farmer: I be here.", ChatHistory.getLastOutput())
    }

    @Test
    fun whereKanbara() {
        CommandParser.parseCommand("w && speak with farmer")
        CommandParser.parseCommand("what is kanbara?")
        assertEquals("Farmer: Kanbara be a city.", ChatHistory.getLastOutput())
    }
}