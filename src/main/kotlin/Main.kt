
import core.GameManager
import core.commands.CommandParsers
import core.events.EventManager
import core.history.TerminalPrinter

fun main(args: Array<String>) {
    EventManager.registerListeners()
    GameManager.newOrLoadGame()
    CommandParsers.parseInitialCommand(args)
    TerminalPrinter.print()
    while (GameManager.playing){
        CommandParsers.parseCommand(readLine() ?: "")
        TerminalPrinter.print()
    }
}