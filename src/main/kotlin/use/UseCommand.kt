package use

import core.commands.*
import core.events.EventManager
import core.history.display
import core.target.Target
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

    override fun execute(source: Target, keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("to", "with", "on")))
        val arguments = Args(args, delimiters)
        val used = source.currentLocation().getTargetsIncludingPlayerInventory(source, arguments.getBaseString()).firstOrNull()
        val target = if (arguments.hasGroup("on")) {
            source.currentLocation().getTargetsIncludingPlayerInventory(source, arguments.getString("on")).firstOrNull()
        } else {
            null
        }

        when {
            arguments.isEmpty() -> clarifyAction()
            arguments.fullString == "item on target" -> clarifyItemForTarget(source)
            arguments.getBaseString() == "item" && !arguments.hasGroup("on") -> clarifyItem(source)
            used == null -> display("Couldn't find $arguments")

            !arguments.hasGroup("on") && args.contains("on") -> clarifyTarget(source, used.name)
            !used.isWithinRangeOf(source) ->  display("You are too far away to use $used.")
            !arguments.hasGroup("on") -> EventManager.postEvent(InteractEvent(source, used))
            target == null -> display("Couldn't find ${arguments.getString("on")}")
            !target.isWithinRangeOf(source) ->  display("You are too far away to use $used on $target.")

            else -> EventManager.postEvent(StartUseEvent(source, used, target))
        }
    }

    private fun clarifyAction() {
        val targets = listOf("Use Item", "Use Item on Target")
        val message = "Do what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { it }))
    }

    private fun clarifyItem(source: Target) {
        val targets = source.currentLocation().getTargetsIncludingPlayerInventory(source).map { it.name }
        val message = "Use what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { "use $it" }))
    }

    private fun clarifyItemForTarget(source: Target) {
        val targets = source.currentLocation().getTargetsIncludingPlayerInventory(source).map { it.name }
        val message = "Use what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { "use $it on" }))
    }

    private fun clarifyTarget(source: Target, used: String) {
        val targets = source.currentLocation().getTargetsIncludingPlayerInventory(source).map { it.name }
        val message = "Use $used on what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { "use $used on $it" }))
    }

}