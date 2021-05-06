package use

import core.commands.*
import core.events.EventManager
import core.history.display
import core.GameState
import use.interaction.InteractEvent

class UseCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Use", "u", "Read")
    }

    override fun getDescription(): String {
        return "Use an item or your surroundings"
    }

    override fun getManual(): String {
        return """
	Use <item> - Interact with an item or target
	Use <item> on <target> - Use an item on a target."""
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override fun execute(keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("to", "with", "on")))
        val arguments = Args(args, delimiters)
        val used = GameState.currentLocation().getTargetsIncludingPlayerInventory(arguments.getBaseString()).firstOrNull()
        val target = if (arguments.hasGroup("on")) {
            GameState.currentLocation().getTargetsIncludingPlayerInventory(arguments.getString("on")).firstOrNull()
        } else {
            null
        }

        when {
            arguments.isEmpty() -> clarifyAction()
            arguments.fullString == "item on target" -> clarifyItemForTarget()
            arguments.getBaseString() == "item" && !arguments.hasGroup("on") -> clarifyItem()
            used == null -> display("Couldn't find $arguments")

            !arguments.hasGroup("on") && args.contains("on") -> clarifyTarget(used.name)
            !used.isWithinRangeOf(GameState.player) ->  display("You are too far away to use $used.")
            !arguments.hasGroup("on") -> EventManager.postEvent(InteractEvent(GameState.player, used))
            target == null -> display("Couldn't find ${arguments.getString("on")}")
            !target.isWithinRangeOf(GameState.player) ->  display("You are too far away to use $used on $target.")

            else -> EventManager.postEvent(StartUseEvent(GameState.player, used, target))
        }
    }

    private fun clarifyAction() {
        val targets = listOf("Use Item", "Use Item on Target")
        val message = "Do what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { it }))
    }

    private fun clarifyItem() {
        val targets = GameState.currentLocation().getTargetsIncludingPlayerInventory().map { it.name }
        val message = "Use what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { "use $it" }))
    }

    private fun clarifyItemForTarget() {
        val targets = GameState.currentLocation().getTargetsIncludingPlayerInventory().map { it.name }
        val message = "Use what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { "use $it on" }))
    }

    private fun clarifyTarget(used: String) {
        val targets = GameState.currentLocation().getTargetsIncludingPlayerInventory().map { it.name }
        val message = "Use $used on what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { "use $used on $it" }))
    }

}