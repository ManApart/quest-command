package performance

import core.GameManager
import core.GameState
import core.commands.CommandParsers
import core.events.EventManager
import core.utility.PoorMansInstrumenter
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Test

class StartupTest {

    @Ignore
    @Test
    fun startupPerformanceTest() {
        runBlocking {
            val timer = PoorMansInstrumenter(10000)
            timer.printElapsed("Starting")
            EventManager.reset()
            timer.printElapsed("Listeners Registered")
            GameManager.newGame()
            timer.printElapsed("New Game Started")
            runBlocking { CommandParsers.parseInitialCommand(GameState.player) }
            timer.printElapsed("Initial Command Parsed")
        }
    }

}