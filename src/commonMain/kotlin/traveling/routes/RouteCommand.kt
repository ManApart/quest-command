package traveling.routes

import core.GameState
import core.Player
import core.commands.Args
import core.commands.Command
import core.events.EventManager
import core.thing.Thing
import core.utility.NameSearchableList
import system.debug.DebugType
import traveling.location.RouteNeighborFinder
import traveling.location.location.LocationManager
import traveling.location.network.LocationNode
import traveling.location.network.NOWHERE_NODE

class RouteCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Route", "rr")
    }

    override fun getDescription(): String {
        return "View your current Route."
    }

    override fun getManual(): String {
        return """
	Route - View your current route.
	Route *<location> - Find a route to <location>.
	Routes are used with the Move command."""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> source.location.getNeighbors().map { it.name }
            else -> listOf()
        }
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args)
        val depth = arguments.getNumber() ?: 5
        val otherArgs = args.minus(depth.toString())

        when {
            otherArgs.isEmpty() -> EventManager.postEvent(ViewRouteEvent(source.thing))
            else -> thingLocation(source.thing, otherArgs, depth, source)
        }
    }

    private fun thingLocation(source: Thing, args: List<String>, depth: Int, player: Player) {
        val locationName = args.joinToString(" ")
        val thing = findThing(source.location, locationName, depth, player)
        if (thing != null) {
            EventManager.postEvent(FindRouteEvent(source, source.location, thing, depth))
        } else {
            println("Could not find ${args.joinToString(" ")} on the map.")
        }
    }

    private fun findThing(source: LocationNode, locationName: String, depth: Int, player: Player): LocationNode? {
        if (LocationManager.getNetwork(source.parent).hasLocation(locationName)) {
            val thing = LocationManager.getNetwork(source.parent).findLocation(locationName)
            if (thing != NOWHERE_NODE) {
                return thing
            }
        }
        val ignoreHidden = !GameState.getDebugBoolean(DebugType.MAP_SHOW_ALL_LOCATIONS)
        return NameSearchableList(RouteNeighborFinder(source, depth, ignoreHidden, ignoreHidden, player).getDestinations()).getOrNull(locationName)
    }

}