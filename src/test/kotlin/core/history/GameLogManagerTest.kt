package core.history

import core.GameState
import core.target.Target
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
    fun trackTarget(){
        val target = Target("Bob")
        val message = "Test Message"
        GameLogger.track(target)
        target.displayToMe(message)
        val history = GameLogger.getHistory(target)
        Assert.assertEquals(message, history.getLastOutput())
    }

    @Test
    fun thing(){
        val target = Target("Bob")
        val target2 = Target("Bob")
        val message = "Test Message"
        GameLogger.track(target)
        GameLogger.track(target2)
        target2.displayToMe(message)
        val history = GameLogger.getHistory(target2)
        Assert.assertEquals(message, history.getLastOutput())
    }
}