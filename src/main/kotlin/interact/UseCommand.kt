package interact

import core.commands.*
import core.gameState.GameState
import core.history.display
import interact.interaction.InteractEvent
import interact.scope.ScopeManager
import system.EventManager

class UseCommand : Command() {
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
        val delimiters = listOf(ArgDelimiter(listOf("to", "with", "on")))
        val arguments = Args(args, delimiters)
        val used = ScopeManager.getScope().getTargetsIncludingPlayerInventory(arguments.getBaseString()).firstOrNull()
        val target = if (arguments.hasGroup("on")) {
            ScopeManager.getScope().getTargetsIncludingPlayerInventory(arguments.getString("on")).firstOrNull()
        } else {
            null
        }

        when {
            arguments.isEmpty() -> clarifyAction()
            arguments.fullString == "item on target" -> clarifyItemForTarget()
            arguments.getBaseString() == "item" && !arguments.hasGroup("on") -> clarifyItem()
            used == null -> display("Couldn't find $arguments")

            !arguments.hasGroup("on") && args.contains("on") -> clarifyTarget(used.name)
            !arguments.hasGroup("on") -> EventManager.postEvent(InteractEvent(GameState.player, used))
            target == null -> display("Couldn't find ${arguments.getString("on")}")

            else -> EventManager.postEvent(UseEvent(GameState.player, used, target))
        }
    }

    private fun clarifyAction() {
        val targets = listOf("Use Item", "Use Item on Target")
        val message = "Do what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.map { it to it }.toMap()))
    }

    private fun clarifyItem() {
        val targets = ScopeManager.getScope().getTargetsIncludingPlayerInventory().map { it.name }
        val message = "Use what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.map { it to "use $it" }.toMap()))
    }

    private fun clarifyItemForTarget() {
        val targets = ScopeManager.getScope().getTargetsIncludingPlayerInventory().map { it.name }
        val message = "Use what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.map { it to "use $it on" }.toMap()))
    }

    private fun clarifyTarget(used: String) {
        val targets = ScopeManager.getScope().getTargetsIncludingPlayerInventory().map { it.name }
        val message = "Use $used on what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.map { it to "use $used on $it" }.toMap()))
    }

}