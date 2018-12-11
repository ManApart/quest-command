package core.history

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ChatHistoryTest {

    @Before
    fun setup(){
        ChatHistory.reset()
    }

    @Test
    fun beforeChatHistoryIsEmpty(){
        Assert.assertEquals(InputOutput().input, ChatHistory.getLastInput())
        Assert.assertEquals("", ChatHistory.getLastOutput())
        Assert.assertEquals(0, ChatHistory.history.size)
    }

    @Test
    fun displayAddsMessageToHistory(){
        val message = "Test Message"
        display(message)
        Assert.assertEquals(InputOutput().input, ChatHistory.getLastInput())
        Assert.assertEquals(message, ChatHistory.getLastOutput())
    }
}