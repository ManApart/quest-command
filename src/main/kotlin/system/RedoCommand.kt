package system

import core.commands.Command
import core.commands.CommandParser
import core.history.ChatHistory
import core.history.display

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

    override fun execute(keyword: String, args: List<String>) {
        val lastCommand = findLastCommand()
        if (lastCommand == null) {
            display("Could not find a command to repeat.")
        } else {
            CommandParser.parseCommand(lastCommand)
        }
    }

    private fun findLastCommand(): String? {
        val history = ChatHistory.history + ChatHistory.getCurrent()
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
        return CommandParser.findCommand(alias) == this
    }

}