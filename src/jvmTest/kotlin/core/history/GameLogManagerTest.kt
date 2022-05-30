package core.history

import core.GameState
import core.Player
import core.thing.Thing

import kotlin.test.BeforeTest


import kotlin.test.assertEquals
import kotlin.test.Test

class GameLogManagerTest {

    @BeforeTest
    fun setup(){
        GameLogger.reset()
    }

    @Test
    fun beforeChatHistoryIsEmpty(){
        val history = GameLogger.getHistory(GameState.player)
        assertEquals(InputOutput().input, history.getLastInput())
        assertEquals("", history.getLastOutput())
        assertEquals(0, history.history.size)
    }

    @Test
    fun displayAddsMessageToHistory(){
        val message = "Test Message"
        displayGlobal(message)
        val history = GameLogger.getMainHistory()
        assertEquals(InputOutput().input, history.getLastInput())
        assertEquals(message, history.getLastOutput())
    }

    @Test
    fun trackThing(){
        val thing = Thing("Bob")
        val player = Player("Player", thing)
        val message = "Test Message"
        GameLogger.track(player)
        player.displayToMe(message)
        val history = GameLogger.getHistory(player)
        assertEquals(message, history.getLastOutput())
    }

    @Test
    fun thing(){
        val thing = Thing("Bob")
        val player = Player("Player", thing)
        val thing2 = Thing("Bob")
        val player2 = Player("Player", thing2)
        val message = "Test Message"
        GameLogger.track(player)
        GameLogger.track(player2)
        player2.displayToMe(message)
        val history = GameLogger.getHistory(player2)
        assertEquals(message, history.getLastOutput())
    }
}