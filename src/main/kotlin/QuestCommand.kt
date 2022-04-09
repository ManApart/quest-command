
import core.GameManager
import core.GameState
import core.commands.CommandParsers
import core.events.EventManager
import core.history.TerminalPrinter

fun main(args: Array<String>) {
    EventManager.registerListeners()
    GameManager.newOrLoadGame()
    CommandParsers.parseInitialCommand(GameState.getPlayer(0)!!, args)
    TerminalPrinter.print()
    while (GameManager.playing){
        CommandParsers.parseCommand(GameState.getPlayer(0)!!, readLine() ?: "")
        TerminalPrinter.print()
    }
}