package explore.look

import core.GameState
import core.Player
import core.history.StringTable
import core.history.displayToMe
import core.target.Target
import core.utility.wrapNonEmpty
import system.debug.DebugType
import traveling.direction.Direction
import traveling.location.Route
import traveling.location.RouteNeighborFinder
import traveling.location.network.LocationNode

fun describeClimbJourney(source: Player) {
    val location = source.target.location
    val distance = getDistance(location).wrapNonEmpty("", " ")
    val exits = getExits(location, source.target.climbTarget!!)
    val exitString = if (exits.isEmpty()) {
        ""
    } else {
        " It is connected to ${exits.joinToString(", ")}."
    }

    source.displayToMe("You are on ${location.name}, ${distance}above the ground.$exitString")
    source.displayToMe(getRoutesString(source, location))
}

private fun getDistance(location: LocationNode): String {
    val distance = location.getDistanceToLowestNodeInNetwork()
    return if (location.isAnOuterNode(Direction.BELOW)) {
        "0 ft"
    } else {
        "$distance ft"
    }
}

private fun getRoutesString(source: Player, location: LocationNode): String {
    val ignoreHidden = !GameState.getDebugBoolean(DebugType.MAP_SHOW_ALL_LOCATIONS)
    val routes = RouteNeighborFinder(location, 1, ignoreHidden, ignoreHidden, source).getNeighbors()

    return if (routes.isNotEmpty()) {
        val input = mutableListOf(listOf("Name", "Distance", "Direction", "Difficulty", "Exits"))
        input.addAll(routes.map { getRouteString(source.target, it) })
        val table = StringTable(input, 2, rightPadding = 2)

        "Options:\n${table.getString()}"
    } else {
        "There is nothing to climb to."
    }
}

private fun getRouteString(source: Target, route: Route): List<String> {
    val exits = getExits(route.destination, source.climbTarget!!)
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