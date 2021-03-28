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
    fun noCriteriaMet() {
        CommandParser.parseCommand("w && speak with farmer")
        CommandParser.parseCommand("why is the sky blue?")
        assertEquals("Farmer: I have nothing to say to you.", ChatHistory.getLastOutput())
    }

    @Test
    fun talkWithFarmer() {
        CommandParser.parseCommand("w && speak with farmer")
        CommandParser.parseCommand("where are you?")
        assertEquals("Farmer: I be here.", ChatHistory.getLastOutput())
    }

    @Test
    fun whereBeMe() {
        CommandParser.parseCommand("w && speak with farmer")
        CommandParser.parseCommand("where am I?")
        assertEquals("Farmer: You be in Farmer's Hut.", ChatHistory.getLastOutput())
    }

    @Test
    fun whatKanbara() {
        CommandParser.parseCommand("w && speak with farmer")
        CommandParser.parseCommand("what is kanbara city?")
        assertEquals("Farmer: Kanbara City be a city.", ChatHistory.getLastOutput())
    }
    //TODO - make work with partial location name?
}