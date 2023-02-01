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

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> source.thing.currentLocation().getThingsIncludingPlayerInventory(source.thing, source.thing).map { it.name }
            args.size == 1 -> listOf("on")
            args.last() == "on" -> source.thing.currentLocation().getThingsIncludingPlayerInventory(source.thing, source.thing).map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
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
            !used.isWithinRangeOf(source.thing) -> source.displayToMe("You are too far away to use ${used.name}.")
            !arguments.hasGroup("on") -> EventManager.postEvent(InteractEvent(source.thing, used))
            thing == null -> source.displayToMe("Couldn't find ${arguments.getString("on")}")
            !thing.isWithinRangeOf(source.thing) -> source.displayToMe("You are too far away to use ${used.name} on ${thing.name}.")

            else -> EventManager.postEvent(startUseEvent(source.thing, used, thing))
        }
    }

    private fun clarifyAction(source: Player) {
        source.respond({}) {
            message("Do what?")
            options("Use Item", "Use Item on Thing")
        }
    }

    private suspend fun clarifyItem(source: Player) {
        source.respondSuspend("There isn't anything here to use.") {
            message("Use what?")
            optionsNamed(source.thing.currentLocation().getThingsIncludingPlayerInventory(source.thing))
            command { "use $it" }
        }
    }

    private suspend fun clarifyItemForThing(source: Player) {
        source.respondSuspend("There isn't anything to use.") {
            message("Use what?")
            optionsNamed(source.thing.currentLocation().getThingsIncludingPlayerInventory(source.thing))
            command { "use $it on" }
        }
    }

    private suspend fun clarifyThing(source: Player, used: String) {
        source.respondSuspend("There isn't anything to use $used on.") {
            message("Use $used on what?")
            optionsNamed(source.thing.currentLocation().getThingsIncludingPlayerInventory(source.thing))
            command { "use $used on $it" }
        }
    }

}