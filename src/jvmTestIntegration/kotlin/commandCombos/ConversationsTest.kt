package commandCombos

import conversation.ConversationCommandInterceptor
import core.GameManager
import core.GameState
import core.commands.CommandParsers
import core.events.EventManager
import core.history.GameLogger
import kotlinx.coroutines.runBlocking
import system.debug.DebugType
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ConversationsTest {

    @BeforeTest
    fun reset() {
        runBlocking {
            EventManager.clear()
            GameManager.newGame(testing = true)
            runBlocking { EventManager.processEvents() }
        }
    }

    @Test
    fun noCriteriaMet() {
        GameState.putDebug(DebugType.RANDOM_RESPONSE, 0)
        runBlocking {
            CommandParsers.parseCommand(GameState.player, "w && speak with farmer")
            CommandParsers.parseCommand(GameState.player, "why is the sky blue?")
        }
        val interceptor = CommandParsers.getParser(GameState.player).commandInterceptor as ConversationCommandInterceptor
        assertEquals("Farmer", interceptor.conversation.getLatestSpeaker().name)
    }

    @Test
    fun whereListener() {
        GameState.putDebug(DebugType.RANDOM_RESPONSE, 0)
        runBlocking {
            CommandParsers.parseCommand(GameState.player, "w && speak with farmer")
            CommandParsers.parseCommand(GameState.player, "where are you?")
        }
        assertEquals("Farmer: I be here.", GameLogger.getMainHistory().getLastOutput())
    }

    @Test
    fun multipleMatchesPicksAtRandom() {
        GameState.putDebug(DebugType.RANDOM_RESPONSE, 1)
        runBlocking {
            CommandParsers.parseCommand(GameState.player, "w && speak with farmer")
            CommandParsers.parseCommand(GameState.player, "where are you?")
        }
        assertEquals("Farmer: I be with you.", GameLogger.getMainHistory().getLastOutput())
    }

    @Test
    fun whereBeMe() {
        runBlocking {
            CommandParsers.parseCommand(GameState.player, "w && speak with farmer")
            CommandParsers.parseCommand(GameState.player, "where am I?")
        }
        assertEquals("Farmer: You be in Farmer's Hut.", GameLogger.getMainHistory().getLastOutput())
    }

    @Test
    fun whatKanbaraCity() {
        runBlocking {
            CommandParsers.parseCommand(GameState.player, "w && speak with farmer")
            CommandParsers.parseCommand(GameState.player, "what is kanbara city?")
        }
        assertEquals("Farmer: Kanbara City be a city.", GameLogger.getMainHistory().getLastOutput())
    }

    @Test
    fun whatLocationWithMultipleMatches() {
        runBlocking {
            CommandParsers.parseCommand(GameState.player, "w && speak with farmer")
            CommandParsers.parseCommand(GameState.player, "what is kanbara?")
        }
        assertEquals("Farmer: What you mean? You mean Kanbara Gate or Kanbara City?", GameLogger.getMainHistory().getLastOutput())
    }
}