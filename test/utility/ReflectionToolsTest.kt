package utility

import org.junit.Assert
import org.junit.Test

class ReflectionToolsTest {

    @Test
    fun getAllCommands(){
        val commands = ReflectionTools.getAllCommands()
        Assert.assertEquals(9, commands.size)
    }

    @Test
    fun getAllEvents(){
        val events = ReflectionTools.getAllEvents()
        Assert.assertEquals(7, events.size)
    }

    @Test
    fun getAllEventListeners(){
        val listeners = ReflectionTools.getAllEventListeners()
        Assert.assertEquals(7, listeners.size)
    }
}