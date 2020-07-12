package explore.look

import traveling.direction.Direction
import core.GameState
import core.target.Target
import traveling.location.location.LocationNode
import traveling.location.Route
import traveling.location.RouteNeighborFinder
import core.history.StringTable
import core.history.display
import core.utility.wrapNonEmpty

fun describeClimbJourney() {
    val location = GameState.player.location
    val distance = getDistance(location).wrapNonEmpty("", " ")
    val exits = getExits(location, GameState.player.climbTarget!!)
    val exitString = if (exits.isEmpty()) {
        ""
    } else {
        " It is connected to ${exits.joinToString(", ")}."
    }

    display("You are on ${location.name}, ${distance}above the ground.$exitString")
    display(getRoutesString(location))
}

private fun getDistance(location: LocationNode): String {
    val distance = location.getDistanceToLowestNodeInNetwork()
    return if (location.isAnOuterNode(Direction.BELOW)) {
        "0 ft"
    } else {
        "$distance ft"
    }
}

private fun getRoutesString(location: LocationNode): String {
    val routes = RouteNeighborFinder(location, 1).getNeighbors()

    return if (routes.isNotEmpty()) {
        val input = mutableListOf(listOf("Name", "Distance", "Direction", "Difficulty", "Exits"))
        input.addAll(routes.map { getRouteString(it) })
        val table = StringTable(input, 2, rightPadding = 2)

        "Options:\n${table.getString()}"
    } else {
        "There is nothing to climb to."
    }
}

private fun getRouteString(route: Route): List<String> {
    val exits = getExits(route.destination, GameState.player.climbTarget!!)
    return listOf(route.destination.name, route.getDistance().toString(), route.getDirectionString(), "1", exits.joinToString(", "))
}

private fun getExits(location: LocationNode, climbTarget: Target): List<String> {
    val dismountLocation = if (climbTarget.body.getClimbEntryParts().contains(location)) {
        climbTarget.location.name
    } else {
        null
    }

    val targets = climbTarget.location.getNeighborConnections()
            .filter { (it.source.targetName == climbTarget.name && it.source.partName == location.name) }
            .map { it.destination.location.name }

    return (targets + dismountLocation).filterNotNull()
}