package commandCombos

import core.GameManager
import core.GameState
import core.commands.CommandParser
import core.events.EventManager
import core.history.ChatHistoryManager
import org.junit.Before
import org.junit.Test
import system.debug.DebugType
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
        GameState.putDebug(DebugType.RANDOM_RESPONSE, 0)
        CommandParser.parseCommand("w && speak with farmer")
        CommandParser.parseCommand("why is the sky blue?")
        assertEquals("Farmer", GameState.conversation.getLatestSpeaker().name)
    }

    @Test
    fun whereListener() {
        GameState.putDebug(DebugType.RANDOM_RESPONSE, 0)
        CommandParser.parseCommand("w && speak with farmer")
        CommandParser.parseCommand("where are you?")
        assertEquals("Farmer: I be here.", ChatHistoryManager.first.getLastOutput())
    }

    @Test
    fun multipleMatchesPicksAtRandom() {
        GameState.putDebug(DebugType.RANDOM_RESPONSE, 1)
        CommandParser.parseCommand("w && speak with farmer")
        CommandParser.parseCommand("where are you?")
        assertEquals("Farmer: I be with you.", ChatHistoryManager.first.getLastOutput())
    }

    @Test
    fun whereBeMe() {
        CommandParser.parseCommand("w && speak with farmer")
        CommandParser.parseCommand("where am I?")
        assertEquals("Farmer: You be in Farmer's Hut.", ChatHistoryManager.first.getLastOutput())
    }

    @Test
    fun whatKanbaraCity() {
        CommandParser.parseCommand("w && speak with farmer")
        CommandParser.parseCommand("what is kanbara city?")
        assertEquals("Farmer: Kanbara City be a city.", ChatHistoryManager.first.getLastOutput())
    }

    @Test
    fun whatLocationWithMultipleMatches() {
        CommandParser.parseCommand("w && speak with farmer")
        CommandParser.parseCommand("what is kanbara?")
        assertEquals("Farmer: What you mean? You mean Kanbara Gate or Kanbara City?", ChatHistoryManager.first.getLastOutput())
    }
}