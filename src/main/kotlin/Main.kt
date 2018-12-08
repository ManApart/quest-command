import core.commands.CommandParser
import system.EventManager
import system.GameManager

fun main(args: Array<String>) {
    EventManager.registerListeners()
    GameManager.newGame()
    CommandParser.parseInitialCommand(args)
    while (GameManager.playing){
        CommandParser.parseCommand(readLine() ?: "")
    }
}
