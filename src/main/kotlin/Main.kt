
import core.GameManager
import core.commands.CommandParser
import core.events.EventManager

fun main(args: Array<String>) {
    EventManager.registerListeners()
    GameManager.newOrLoadGame()
    CommandParser.parseInitialCommand(args)
    while (GameManager.playing){
        CommandParser.parseCommand(readLine() ?: "")
    }
}