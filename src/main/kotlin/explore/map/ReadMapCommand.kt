package explore.map

import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.respond
import core.events.EventManager
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

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args)
        val depth = arguments.getNumber() ?: 1
        val otherArgs = args.minus(depth.toString())

        when{
            arguments.isEmpty() && keyword == "map" -> clarifyDepth(source)
            otherArgs.isEmpty() -> currentLocation(source, depth)
            else -> thingLocation(source, otherArgs, depth)
        }
    }

    private fun clarifyDepth(player: Player) {
        player.respond({}) {
            message("View how many hops?")
            options("1", "3", "5", "10", "20")
            command { "map $it" }
        }
    }

    private fun currentLocation(source: Player, depth: Int){
        EventManager.postEvent(ReadMapEvent(source, source.thing.location, depth))
    }

    private fun thingLocation(source: Player, args: List<String>, depth: Int){
        val thing = LocationManager.findLocationInAnyNetwork(source.thing, args.joinToString(" "))
        if (thing != null) {
            EventManager.postEvent(ReadMapEvent(source, thing, depth))
        } else {
            println("Could not find ${args.joinToString(" ")} on the map.")
        }
    }

}