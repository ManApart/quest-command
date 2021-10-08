package core.history

import core.GameState
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ChatHistoryManagerTest {

    @Before
    fun setup(){
        ChatHistoryManager.reset()
    }

    @Test
    fun beforeChatHistoryIsEmpty(){
        val history = ChatHistoryManager.getHistory(GameState.player)
        Assert.assertEquals(InputOutput().input, history.getLastInput())
        Assert.assertEquals("", history.getLastOutput())
        Assert.assertEquals(0, history.history.size)
    }

    @Test
    fun displayAddsMessageToHistory(){
        val message = "Test Message"
        display(message)
        val history = ChatHistoryManager.getHistory(GameState.player)
        Assert.assertEquals(InputOutput().input, history.getLastInput())
        Assert.assertEquals(message, history.getLastOutput())
    }
}