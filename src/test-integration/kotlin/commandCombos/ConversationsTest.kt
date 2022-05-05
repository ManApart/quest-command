package commandCombos

import conversation.ConversationCommandInterceptor
import core.GameManager
import core.GameState
import core.commands.CommandParsers
import core.events.EventManager
import core.history.GameLogger
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
        CommandParsers.parseCommand(GameState.player, "w && speak with farmer")
        CommandParsers.parseCommand(GameState.player, "why is the sky blue?")
        val interceptor= CommandParsers.getParser(GameState.player).commandInterceptor as ConversationCommandInterceptor
        assertEquals("Farmer", interceptor.conversation.getLatestSpeaker().name)
    }

    @Test
    fun whereListener() {
        GameState.putDebug(DebugType.RANDOM_RESPONSE, 0)
        CommandParsers.parseCommand(GameState.player, "w && speak with farmer")
        CommandParsers.parseCommand(GameState.player, "where are you?")
        assertEquals("Farmer: I be here.", GameLogger.getMainHistory().getLastOutput())
    }

    @Test
    fun multipleMatchesPicksAtRandom() {
        GameState.putDebug(DebugType.RANDOM_RESPONSE, 1)
        CommandParsers.parseCommand(GameState.player, "w && speak with farmer")
        CommandParsers.parseCommand(GameState.player, "where are you?")
        assertEquals("Farmer: I be with you.", GameLogger.getMainHistory().getLastOutput())
    }

    @Test
    fun whereBeMe() {
        CommandParsers.parseCommand(GameState.player, "w && speak with farmer")
        CommandParsers.parseCommand(GameState.player, "where am I?")
        assertEquals("Farmer: You be in Farmer's Hut.", GameLogger.getMainHistory().getLastOutput())
    }

    @Test
    fun whatKanbaraCity() {
        CommandParsers.parseCommand(GameState.player, "w && speak with farmer")
        CommandParsers.parseCommand(GameState.player, "what is kanbara city?")
        assertEquals("Farmer: Kanbara City be a city.", GameLogger.getMainHistory().getLastOutput())
    }

    @Test
    fun whatLocationWithMultipleMatches() {
        CommandParsers.parseCommand(GameState.player, "w && speak with farmer")
        CommandParsers.parseCommand(GameState.player, "what is kanbara?")
        assertEquals("Farmer: What you mean? You mean Kanbara Gate or Kanbara City?", GameLogger.getMainHistory().getLastOutput())
    }
}