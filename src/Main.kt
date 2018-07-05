import gameState.ScopeManager
import processing.ActionManager
import processing.EventManager
import processing.GameManager
import processing.ItemManager

fun main(args: Array<String>) {
    GameManager.newGame()
    preWarm()
    while (true){
        CommandParser.parseCommand(readLine() ?: "")
        EventManager.executeEvents()
    }
}

private fun preWarm(){
    //Initialize these objects on startup so the first command isn't slow
    ActionManager.toString()
    EventManager.toString()
    ItemManager.toString()
    ScopeManager.toString()
}