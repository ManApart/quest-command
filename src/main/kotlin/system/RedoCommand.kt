package system

import core.Player
import core.commands.Command
import core.commands.CommandParsers
import core.history.GameLogger
import core.history.displayToMe

class RedoCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Redo", "Repeat", "r")
    }

    override fun getDescription(): String {
        return "Redo your last command."
    }

    override fun getManual(): String {
        return "Redo:\n\tRedo your last command."
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val lastCommand = findLastCommand(source)
        if (lastCommand == null) {
            source.displayToMe("Could not find a command to repeat.")
        } else {
            CommandParsers.parseCommand(source, lastCommand)
        }
    }

    private fun findLastCommand(source: Player): String? {
        val chatHistory = GameLogger.getHistory(source)
        val history = chatHistory.history + chatHistory.getCurrent()
        for (i in history.size - 1 downTo 0) {
            val commands = history[i].input
                    .split("&&")
                    .filter { !isRedoCommand(it) }
            if (commands.isNotEmpty()) {
                return commands.joinToString("&&")
            }
        }
        return null
    }

    private fun isRedoCommand(command: String): Boolean {
        val alias = command.trim().split(" ")[0]
        return CommandParsers.findCommand(alias) == this
    }

}