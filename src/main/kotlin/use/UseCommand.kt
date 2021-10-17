package use

import core.commands.*
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
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

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("to", "with", "on")))
        val arguments = Args(args, delimiters)
        val used = source.currentLocation().getThingsIncludingPlayerInventory(source, arguments.getBaseString()).firstOrNull()
        val thing = if (arguments.hasGroup("on")) {
            source.currentLocation().getThingsIncludingPlayerInventory(source, arguments.getString("on")).firstOrNull()
        } else {
            null
        }

        when {
            arguments.isEmpty() -> clarifyAction()
            arguments.fullString == "item on thing" -> clarifyItemForThing(source)
            arguments.getBaseString() == "item" && !arguments.hasGroup("on") -> clarifyItem(source)
            used == null -> source.displayToMe("Couldn't find $arguments")

            !arguments.hasGroup("on") && args.contains("on") -> clarifyThing(source, used.name)
            !used.isWithinRangeOf(source) -> source.displayToMe("You are too far away to use $used.")
            !arguments.hasGroup("on") -> EventManager.postEvent(InteractEvent(source, used))
            thing == null -> source.displayToMe("Couldn't find ${arguments.getString("on")}")
            !thing.isWithinRangeOf(source) -> source.displayToMe("You are too far away to use $used on $thing.")

            else -> EventManager.postEvent(StartUseEvent(source, used, thing))
        }
    }

    private fun clarifyAction() {
        val things = listOf("Use Item", "Use Item on Thing")
        val message = "Do what?\n\t${things.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, things.associateWith { it }))
    }

    private fun clarifyItem(source: Thing) {
        val things = source.currentLocation().getThingsIncludingPlayerInventory(source).map { it.name }
        val message = "Use what?\n\t${things.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, things.associateWith { "use $it" }))
    }

    private fun clarifyItemForThing(source: Thing) {
        val things = source.currentLocation().getThingsIncludingPlayerInventory(source).map { it.name }
        val message = "Use what?\n\t${things.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, things.associateWith { "use $it on" }))
    }

    private fun clarifyThing(source: Thing, used: String) {
        val things = source.currentLocation().getThingsIncludingPlayerInventory(source).map { it.name }
        val message = "Use $used on what?\n\t${things.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, things.associateWith { "use $used on $it" }))
    }

}