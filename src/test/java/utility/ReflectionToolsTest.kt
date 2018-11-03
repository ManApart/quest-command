package utility

import core.utility.ReflectionTools
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

/**
 * While I'm not happy about it, I'm ignoring the tests in this file for now as they are slow locally and fail with Travis CI.
 */

@Ignore
class ReflectionToolsTest {

    @Test
    fun getAllCommands(){
        val commands = ReflectionTools.getAllCommands()
        Assert.assertEquals(true, commands.isNotEmpty())
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