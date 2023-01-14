package core.commands

import core.AUTO_SAVE
import core.GameState
import core.Player
import core.history.SessionHistory
import core.history.displayToMe
import core.thing.Thing

class UnknownCommand : Command() {

    override fun getAliases(): List<String> {
        return listOf("")
    }

    override fun getDescription(): String {
        return "Called when no other command is present"
    }

    override fun getManual(): String {
        return "This is the manual for an Unknown Command"
    }

    override fun getCategory(): List<String> {
        return listOf("")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return listOf()
    }

    suspend fun execute(source: Player, args: List<String>) {
        execute(source.thing, "", args)
    }

    override suspend fun execute(source: Thing, keyword: String, args: List<String>) {
        val line = args.joinToString(" ")
        if (line.isNotBlank()) {
            SessionHistory.addUnknownCommand(line)
            if (GameState.properties.values.getBoolean(AUTO_SAVE)){
                SessionHistory.saveSessionStats()
            }
            source.displayToMe("Unknown command: $line")
        }
    }
}