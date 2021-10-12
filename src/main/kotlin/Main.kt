
import core.GameManager
import core.commands.CommandParser
import core.events.EventManager
import core.history.TerminalPrinter

fun main(args: Array<String>) {
    EventManager.registerListeners()
    GameManager.newOrLoadGame()
    CommandParser.parseInitialCommand(args)
    TerminalPrinter.print()
    while (GameManager.playing){
        CommandParser.parseCommand(readLine() ?: "")
        TerminalPrinter.print()
    }
}