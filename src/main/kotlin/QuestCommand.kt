import core.GameManager
import core.GameState
import core.commands.CommandParsers
import core.events.EventManager
import core.history.TerminalPrinter

fun main(args: Array<String>) {
    EventManager.registerListeners()
    GameManager.newOrLoadGame()
    CommandParsers.parseInitialCommand(GameState.player, args)
    TerminalPrinter.print()
    while (GameManager.playing) {
        CommandParsers.parseCommand(GameState.player, readLine() ?: "")
        TerminalPrinter.print()
    }
}