package performance

import core.GameManager
import core.GameState
import core.commands.CommandParsers
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
        CommandParsers.parseInitialCommand(GameState.player, arrayOf())
        timer.printElapsed("Initial Command Parsed")
    }

}