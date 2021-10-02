package core.commands

import core.AUTO_SAVE
import core.GameState
import core.history.SessionHistory
import core.history.display
import core.target.Target

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

    fun execute(args: List<String>) {
        execute(GameState.player, "", args)
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        val line = args.joinToString(" ")
        if (line.isNotBlank()) {
            SessionHistory.addUnknownCommand(line)
            if (GameState.properties.values.getBoolean(AUTO_SAVE)){
                SessionHistory.saveSessionStats()
            }
            display("Unknown command: $line")
        }
    }
}