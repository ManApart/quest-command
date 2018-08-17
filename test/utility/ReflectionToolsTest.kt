package utility

import core.utility.ReflectionTools
import org.junit.Assert
import org.junit.Test

class ReflectionToolsTest {

    @Test
    fun getAllCommands(){
        val commands = ReflectionTools.getAllCommands()
        Assert.assertTrue(commands.isNotEmpty())
    }

    @Test
    fun getAllEvents(){
        val events = ReflectionTools.getAllEvents()
        Assert.assertTrue(events.isNotEmpty())
    }

    @Test
    fun getAllEventListeners(){
        val listeners = ReflectionTools.getAllEventListeners()
        Assert.assertTrue(listeners.isNotEmpty())
    }
}