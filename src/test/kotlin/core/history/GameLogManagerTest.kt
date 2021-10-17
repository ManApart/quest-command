package core.history

import core.GameState
import core.Player
import core.thing.Thing
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GameLogManagerTest {

    @Before
    fun setup(){
        GameLogger.reset()
    }

    @Test
    fun beforeChatHistoryIsEmpty(){
        val history = GameLogger.getHistory(GameState.player)
        Assert.assertEquals(InputOutput().input, history.getLastInput())
        Assert.assertEquals("", history.getLastOutput())
        Assert.assertEquals(0, history.history.size)
    }

    @Test
    fun displayAddsMessageToHistory(){
        val message = "Test Message"
        display(message)
        val history = GameLogger.getHistory(GameState.player)
        Assert.assertEquals(InputOutput().input, history.getLastInput())
        Assert.assertEquals(message, history.getLastOutput())
    }

    @Test
    fun trackThing(){
        val thing = Thing("Bob")
        val player = Player(2, thing)
        val message = "Test Message"
        GameLogger.track(player)
        player.displayToMe(message)
        val history = GameLogger.getHistory(player)
        Assert.assertEquals(message, history.getLastOutput())
    }

    @Test
    fun thing(){
        val thing = Thing("Bob")
        val player = Player(2, thing)
        val thing2 = Thing("Bob")
        val player2 = Player(2, thing2)
        val message = "Test Message"
        GameLogger.track(player)
        GameLogger.track(player2)
        player2.displayToMe(message)
        val history = GameLogger.getHistory(player2)
        Assert.assertEquals(message, history.getLastOutput())
    }
}