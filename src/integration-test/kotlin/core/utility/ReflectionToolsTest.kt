package core.utility

import org.junit.Assert
import org.junit.Test

class ReflectionToolsTest {

    @Test
    fun getAllCommands(){
        val commands = ReflectionTools.commands
        Assert.assertEquals(true, commands.isNotEmpty())
    }

    @Test
    fun getAllEvents(){
        val events = ReflectionTools.events
        Assert.assertEquals(true, events.isNotEmpty())
    }

    @Test
    fun getAllEventListeners(){
        val listeners = ReflectionTools.eventListeners
        Assert.assertEquals(true, listeners.isNotEmpty())
    }
}