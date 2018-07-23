import core.commands.CommandParser
import core.gameState.GameState
import system.EventManager
import system.GameManager
import travel.ArriveEvent

fun main(args: Array<String>) {
    GameManager.newGame()
    EventManager.postEvent(ArriveEvent(destination = GameState.player.creature.location, method = "wake"))
    CommandParser.parseCommand("map")
    while (true){
        CommandParser.parseCommand(readLine() ?: "")
    }
}
