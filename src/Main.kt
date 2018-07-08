import core.commands.CommandParser
import system.EventManager
import system.GameManager

fun main(args: Array<String>) {
    GameManager.newGame()
    while (true){
        CommandParser.parseCommand(readLine() ?: "")
        EventManager.executeEvents()
    }
}
