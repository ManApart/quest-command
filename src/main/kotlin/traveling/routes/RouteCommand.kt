package traveling.routes

import core.commands.Args
import core.commands.Command
import core.events.EventManager
import core.target.Target
import core.utility.NameSearchableList
import traveling.location.RouteNeighborFinder
import traveling.location.location.LocationManager
import traveling.location.location.LocationNode
import traveling.location.location.NOWHERE_NODE

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

    override fun execute(source: Target, keyword: String, args: List<String>) {
        val arguments = Args(args)
        val depth = arguments.getNumber() ?: 5
        val otherArgs = args.minus(depth.toString())

        when {
            otherArgs.isEmpty() -> EventManager.postEvent(ViewRouteEvent())
            else -> targetLocation(source, otherArgs, depth)
        }
    }

    private fun targetLocation(source: Target, args: List<String>, depth: Int) {
        val locationName = args.joinToString(" ")
        val target = findTarget(source.location, locationName, depth)
        if (target != null) {
            EventManager.postEvent(FindRouteEvent(source.location, target, depth))
        } else {
            println("Could not find ${args.joinToString(" ")} on the map.")
        }
    }

    private fun findTarget(source: LocationNode, locationName: String, depth: Int): LocationNode? {
        if (LocationManager.getNetwork().hasLocation(locationName)) {
            val target = LocationManager.getNetwork().findLocation(locationName)
            if (target != NOWHERE_NODE) {
                return target
            }
        }
        return NameSearchableList(RouteNeighborFinder(source, depth).getDestinations()).getOrNull(locationName)
    }

}