package use

import core.Player
import core.commands.*
import core.events.EventManager
import core.history.displayToMe
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
	Use <item> - Interact with an item or thing
	Use <item> on <thing> - Use an item on a thing."""
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("to", "with", "on")))
        val arguments = Args(args, delimiters)
        val used = source.thing.currentLocation().getThingsIncludingPlayerInventory(source.thing, arguments.getBaseString()).firstOrNull()
        val thing = if (arguments.hasGroup("on")) {
            source.thing.currentLocation().getThingsIncludingPlayerInventory(source.thing, arguments.getString("on")).firstOrNull()
        } else {
            null
        }

        when {
            arguments.isEmpty() -> clarifyAction(source)
            arguments.fullString == "item on thing" -> clarifyItemForThing(source)
            arguments.getBaseString() == "item" && !arguments.hasGroup("on") -> clarifyItem(source)
            used == null -> source.displayToMe("Couldn't find $arguments")

            !arguments.hasGroup("on") && args.contains("on") -> clarifyThing(source, used.name)
            !used.isWithinRangeOf(source.thing) -> source.displayToMe("You are too far away to use $used.")
            !arguments.hasGroup("on") -> EventManager.postEvent(InteractEvent(source.thing, used))
            thing == null -> source.displayToMe("Couldn't find ${arguments.getString("on")}")
            !thing.isWithinRangeOf(source.thing) -> source.displayToMe("You are too far away to use $used on $thing.")

            else -> EventManager.postEvent(StartUseEvent(source.thing, used, thing))
        }
    }

    private fun clarifyAction(source: Player) {
        val things = listOf("Use Item", "Use Item on Thing")
        val message = "Do what?\n\t${things.joinToString(", ")}"
        CommandParsers.setResponseRequest(source, ResponseRequest(message, things.associateWith { it }))
    }

    private fun clarifyItem(source: Player) {
        val things = source.thing.currentLocation().getThingsIncludingPlayerInventory(source.thing).map { it.name }
        val message = "Use what?\n\t${things.joinToString(", ")}"
        CommandParsers.setResponseRequest(source, ResponseRequest(message, things.associateWith { "use $it" }))
    }

    private fun clarifyItemForThing(source: Player) {
        val things = source.thing.currentLocation().getThingsIncludingPlayerInventory(source.thing).map { it.name }
        val message = "Use what?\n\t${things.joinToString(", ")}"
        CommandParsers.setResponseRequest(source, ResponseRequest(message, things.associateWith { "use $it on" }))
    }

    private fun clarifyThing(source: Player, used: String) {
        val things = source.thing.currentLocation().getThingsIncludingPlayerInventory(source.thing).map { it.name }
        val message = "Use $used on what?\n\t${things.joinToString(", ")}"
        CommandParsers.setResponseRequest(source, ResponseRequest(message, things.associateWith { "use $used on $it" }))
    }

}