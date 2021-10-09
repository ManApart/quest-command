
import core.GameManager
import core.commands.CommandParser
import core.events.EventManager
import core.history.ChatHistoryManager

fun main(args: Array<String>) {
    EventManager.registerListeners()
    GameManager.newOrLoadGame()
    CommandParser.parseInitialCommand(args)
    ChatHistoryManager.flushHistories()
    while (GameManager.playing){
        CommandParser.parseCommand(readLine() ?: "")
        ChatHistoryManager.flushHistories()
    }
}