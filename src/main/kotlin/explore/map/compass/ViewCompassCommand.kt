package explore.map.compass

import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.CommandParsers
import core.commands.ResponseRequest
import core.events.EventManager

class ViewCompassCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Compass", "co")
    }

    override fun getDescription(): String {
        return "View the general direction toward your goal."
    }

    override fun getManual(): String {
        return """
    Compass - View the general direction toward your goal.
	Compass <location> *depth - Set your compass goal, optionally passing a search depth."""
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        //TODO - replace commands with Player instead of thing
        val arguments = Args(args)
        val isAlias = keyword != "compass"
        val specifiedDepth = arguments.getNumber() != null
        val depth = arguments.getNumber() ?: 50
        val locationName = args.minus(depth.toString()).joinToString(" ")
        val existingDestination = source.compassRoute?.destination

        when {
            locationName.isBlank() && existingDestination != null -> EventManager.postEvent(ViewCompassEvent(source))
            locationName.isNotBlank() && specifiedDepth -> EventManager.postEvent(SetCompassEvent(source, locationName, depth))
            locationName.isNotBlank() && isAlias -> EventManager.postEvent(SetCompassEvent(source, locationName, depth))
            locationName.isNotBlank() -> clarifyDepth(source, locationName)
            else -> println("Point compass to what goal?")
        }
    }

    private fun clarifyDepth(source: Player, locationArgs: String) {
        val things = listOf("1", "3", "5", "10", "20")
        val message = "Search how far?\n\t${things.joinToString(", ")}"
        CommandParsers.setResponseRequest(source, ResponseRequest(message, things.associateWith { "compass $locationArgs $it" }))
    }

}