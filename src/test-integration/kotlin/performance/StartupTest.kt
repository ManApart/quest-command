package performance

import core.GameManager
import core.commands.CommandParser
import core.events.EventManager
import core.utility.PoorMansInstrumenter
import org.junit.Ignore
import org.junit.Test

class StartupTest {

    @Ignore
    @Test
    fun startupPerformanceTest() {
        val timer = PoorMansInstrumenter(10000)
        timer.printElapsed("Starting")
        EventManager.registerListeners()
        timer.printElapsed("Listeners Registered")
        GameManager.newGame()
        timer.printElapsed("New Game Started")
        CommandParser.parseInitialCommand(arrayOf())
        timer.printElapsed("Initial Command Parsed")
    }

}