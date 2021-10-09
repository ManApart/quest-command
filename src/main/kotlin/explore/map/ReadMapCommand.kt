package explore.map

import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.target.Target
import traveling.location.location.LocationManager

class ReadMapCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Map", "m")
    }

    override fun getDescription(): String {
        return "Get information on your current and other locations."
    }

    override fun getManual(): String {
        return """
	Map *<location> - List your current location (or given location) and the surrounding areas.
	Map *depth - List neighbors to <depth> levels away from the location."""
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        val arguments = Args(args)
        val depth = arguments.getNumber() ?: 1
        val otherArgs = args.minus(depth.toString())

        when{
            arguments.isEmpty() && keyword == "map" -> clarifyDepth()
            otherArgs.isEmpty() -> currentLocation(source, depth)
            else -> targetLocation(source, otherArgs, depth)
        }
    }

    private fun clarifyDepth() {
        val targets = listOf("1", "3", "5", "10", "20")
        val message = "View how many hops?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { "map $it" }))
    }

    private fun currentLocation(source: Target, depth: Int){
        EventManager.postEvent(ReadMapEvent(source, source.location, depth))
    }

    private fun targetLocation(source: Target, args: List<String>, depth: Int){
        val target = LocationManager.findLocationInAnyNetwork(source, args.joinToString(" "))
        if (target != null) {
            EventManager.postEvent(ReadMapEvent(source, target, depth))
        } else {
            println("Could not find ${args.joinToString(" ")} on the map.")
        }
    }

}