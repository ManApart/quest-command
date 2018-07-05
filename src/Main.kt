import processing.EventManager
import processing.GameManager

fun main(args: Array<String>) {
    GameManager.newGame()
    while (true){
        CommandParser.parseCommand(readLine() ?: "")
        EventManager.executeEvents()
    }
}