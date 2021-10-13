package traveling.routes

import core.GameState
import core.GameState.player
import core.Player
import core.commands.Args
import core.commands.Command
import core.events.EventManager
import core.target.Target
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

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args)
        val depth = arguments.getNumber() ?: 5
        val otherArgs = args.minus(depth.toString())

        when {
            otherArgs.isEmpty() -> EventManager.postEvent(ViewRouteEvent(source.target))
            else -> targetLocation(source.target, otherArgs, depth, player)
        }
    }

    private fun targetLocation(source: Target, args: List<String>, depth: Int, player: Player) {
        val locationName = args.joinToString(" ")
        val target = findTarget(source.location, locationName, depth, player)
        if (target != null) {
            EventManager.postEvent(FindRouteEvent(source, source.location, target, depth))
        } else {
            println("Could not find ${args.joinToString(" ")} on the map.")
        }
    }

    private fun findTarget(source: LocationNode, locationName: String, depth: Int, player: Player): LocationNode? {
        if (LocationManager.getNetwork(source.parent).hasLocation(locationName)) {
            val target = LocationManager.getNetwork(source.parent).findLocation(locationName)
            if (target != NOWHERE_NODE) {
                return target
            }
        }
        val ignoreHidden = !GameState.getDebugBoolean(DebugType.MAP_SHOW_ALL_LOCATIONS)
        return NameSearchableList(RouteNeighborFinder(source, depth, ignoreHidden, ignoreHidden, player).getDestinations()).getOrNull(locationName)
    }

}