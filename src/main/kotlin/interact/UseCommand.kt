package interact

import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.GameState
import core.history.display
import interact.interaction.InteractEvent
import interact.scope.ScopeManager
import system.EventManager

class UseCommand : Command() {
    private val delimiters = listOf("to", "with", "on")
    override fun getAliases(): Array<String> {
        return arrayOf("Use", "u", "Read")
    }

    override fun getDescription(): String {
        return "Use:\n\tUse an item or your surroundings"
    }

    override fun getManual(): String {
        return "\n\tUse <item> - Interact with an item or target" +
                "\n\tUse <item> on <target> - Use an item on a target."
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters)
        val used = ScopeManager.getScope().getTargetsIncludingPlayerInventory(arguments.argStrings[0]).firstOrNull()
        val target = if (arguments.argGroups.size > 1) {
            ScopeManager.getScope().getTargetsIncludingPlayerInventory(arguments.argStrings[1]).firstOrNull()
        } else {
            null
        }

        when {
            arguments.isEmpty() -> clarifyAction()
            arguments.fullString == "item on target" -> clarifyItemForTarget()
            arguments.argStrings.size == 1 && arguments.argStrings[0] == "item" -> clarifyItem()
            used == null -> display("Couldn't find $arguments")

            arguments.argStrings.size <= 1 -> EventManager.postEvent(InteractEvent(GameState.player, used))
            arguments.argStrings.size > 1 && arguments.argStrings[1].isBlank() -> clarifyTarget(used.name)
            target == null -> display("Couldn't find ${arguments.argStrings[1]}")

            else -> EventManager.postEvent(UseEvent(GameState.player, used, target))
        }
    }

    private fun clarifyAction() {
        val targets = listOf("Use Item", "Use Item on Target")
        display("Do what?\n\t${targets.joinToString(", ")}")
        CommandParser.responseRequest = ResponseRequest(targets.map { it to it }.toMap())
    }

    private fun clarifyItem() {
        val targets = ScopeManager.getScope().getTargetsIncludingPlayerInventory().map { it.name }
        display("Use what?\n\t${targets.joinToString(", ")}")
        CommandParser.responseRequest = ResponseRequest(targets.map { it to "use $it" }.toMap())
    }

    private fun clarifyItemForTarget() {
        val targets = ScopeManager.getScope().getTargetsIncludingPlayerInventory().map { it.name }
        display("Use what?\n\t${targets.joinToString(", ")}")
        CommandParser.responseRequest = ResponseRequest(targets.map { it to "use $it on" }.toMap())
    }

    private fun clarifyTarget(used: String) {
        val targets = ScopeManager.getScope().getTargetsIncludingPlayerInventory().map { it.name }
        display("Use $used on what?\n\t${targets.joinToString(", ")}")
        CommandParser.responseRequest = ResponseRequest(targets.map { it to "use $used on $it" }.toMap())
    }

}