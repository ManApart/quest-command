package inventory

import core.Player
import core.commands.Command
import core.commands.respond
import core.events.EventManager
import core.thing.Thing

class EquippedCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Equipped", "eqd")
    }

    override fun getDescription(): String {
        return "View what you currently have equipped"
    }

    override fun getManual(): String {
        return """
	Equipped - View what you currently have equipped
	Equipped <target> - View what <target> currently has equipped
    """
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> source.thing.currentLocation().getActivators(perceivedBy = source.thing).map { it.name } + source.thing.currentLocation().getCreatures(perceivedBy = source.thing).map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        val possibleTargets = if (argString.isEmpty()) {
            source.thing.currentLocation().getCreatures()
        } else {
            source.thing.currentLocation().getCreatures(argString)
        }

        when {
            possibleTargets.size == 1 -> EventManager.postEvent(ViewEquippedEvent(source, possibleTargets.first()))
            possibleTargets.size > 1 && (keyword == "equipped" || args.isNotEmpty()) -> clarifyThing(source, possibleTargets)
            else -> EventManager.postEvent(ViewEquippedEvent(source, source.thing))
        }
    }

    private fun clarifyThing(source: Player, things: List<Thing>) {
        source.respond({ EventManager.postEvent(ViewEquippedEvent(source, source.thing)) }) {
            message("View whose equipped items?")
            optionsNamed(things)
            command { "equipped $it" }
        }
    }

}