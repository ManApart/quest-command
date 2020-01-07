package system.persistance.rename

import core.GameState
import core.commands.*
import core.events.EventManager
import core.history.display
import traveling.scope.ScopeManager

class RenameCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Rename", "rn")
    }

    override fun getDescription(): String {
        return "Rename:\n\tRename yourself or an item."
    }

    override fun getManual(): String {
        return "\n\tRename <target>* to <name> - Rename the target, or yourself if target is empty."
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf(ArgDelimiter("to")))
        val name = arguments.getString("to")
        val targets = ScopeManager.getScope().getTargetsIncludingPlayerInventory(arguments.getBaseString())
        val tooManyTargetsResponse = ResponseRequest("Rename what?\n\t${targets.joinToString(", ") { it.getDisplayName() }}", targets.map { it.name to "Rename ${it.name} to $name" }.toMap())
        when {
            targets.size > 1 -> CommandParser.setResponseRequest(tooManyTargetsResponse)
            name.isBlank() && targets.isEmpty() && arguments.getBaseString().isNotBlank() -> EventManager.postEvent(RenameEvent(GameState.player, arguments.getBaseString()))
            name.isBlank() && targets.isEmpty() -> display("Rename who to what?")
            name.isBlank() -> display("Rename ${targets.first()} to what?")
            targets.isEmpty() -> EventManager.postEvent(RenameEvent(GameState.player, name))
            else -> EventManager.postEvent(RenameEvent(targets.first(), name))
        }

    }
}