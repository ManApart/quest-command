import core.commands.CommandParser
import core.gameState.GameState
import system.EventManager
import system.GameManager
import system.GameStartEvent
import travel.ArriveEvent

fun main(args: Array<String>) {
    EventManager.registerListeners()
    GameManager.newGame()
    EventManager.postEvent(GameStartEvent())
    EventManager.postEvent(ArriveEvent(destination = GameState.player.creature.location, method = "wake"))
    val initialCommand = if (args.isEmpty()) {
        "look"
    } else {
        args.joinToString(" ")
    }
    CommandParser.parseCommand(initialCommand)
    while (true){
        CommandParser.parseCommand(readLine() ?: "")
    }
}
