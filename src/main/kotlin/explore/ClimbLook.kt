package explore

import core.gameState.Direction
import core.gameState.GameState
import core.gameState.location.*
import core.history.StringTable
import core.history.display
import core.utility.wrapNonEmpty

object ClimbLook {

    fun describeClimbJourney() {
        val location = GameState.player.location
        val distance = getDistance(location).wrapNonEmpty("", " ")

        display("You are on ${location.name}, $distance above the ground.")
        display(getRoutesString(location))
    }

    private fun getDistance(location: LocationNode): String {
        val lowestNode = location.getNetwork().getFurthestLocations(Direction.BELOW).first()
        val route = RouteFinder(location, lowestNode)
        return if (route.hasRoute()) {
            "${route.getRoute().getDistance()} ft"
        } else {
            "an unknown distance"
        }
    }

    private fun getRoutesString(location: LocationNode): String {
        val routes = RouteNeighborFinder(location, 1).getNeighbors()

        return if (routes.isNotEmpty()) {
            val input = mutableListOf(listOf("Name", "Distance", "Direction", "Difficulty"))
            input.addAll(routes.map { getRouteString(it) })
            val table = StringTable(input, 2, rightPadding = 2)

            "Options:\n${table.getString()}"
        } else {
            "There is nothing to climb to."
        }
    }

    private fun getRouteString(route: Route): List<String> {
        return listOf(route.destination.name, route.getDistance().toString(), route.getDirectionString(), "1")
    }
}