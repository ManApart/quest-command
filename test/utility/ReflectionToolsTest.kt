package utility

import core.utility.ReflectionTools
import org.junit.Assert
import org.junit.Test


class ReflectionToolsTest {

    @Test
    fun getAllCommands(){
        val commands = ReflectionTools.getAllCommands()
        Assert.assertEquals(1, commands.isNotEmpty())
    }

    @Test
    fun getAllEvents(){
        val events = ReflectionTools.getAllEvents()
        Assert.assertEquals(true, events.isNotEmpty())
    }

    @Test
    fun getAllEventListeners(){
        val listeners = ReflectionTools.getAllEventListeners()
        Assert.assertEquals(true, listeners.isNotEmpty())
    }
}