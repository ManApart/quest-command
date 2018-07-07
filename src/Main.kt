import core.commands.CommandParser
import interact.ScopeManager
import interact.ActionManager
import system.EventManager
import system.GameManager
import system.ItemManager

fun main(args: Array<String>) {
    GameManager.newGame()
    while (true){
        CommandParser.parseCommand(readLine() ?: "")
        EventManager.executeEvents()
    }
}
