package explore.look

import core.GameState
import core.Player
import core.history.StringTable
import core.history.displayToMe
import core.history.displayToOthers
import core.thing.Thing
import core.utility.wrapNonEmpty
import system.debug.DebugType
import traveling.direction.Direction
import traveling.location.Route
import traveling.location.RouteNeighborFinder
import traveling.location.network.LocationNode

fun describeClimbJourney(source: Player, detailed: Boolean = false) {
    val location = source.thing.location
    val distance = getDistance(location).wrapNonEmpty("", " ")
    val exits = getExits(location, source.thing.climbThing!!)
    val exitString = if (exits.isEmpty()) {
        ""
    } else {
        " It is connected to ${exits.joinToString(", ")}."
    }

    source.displayToMe("You are on ${location.name}, ${distance}above the ground.$exitString")
    if (detailed) source.displayToMe("${location.name} is made of ${location.getLocation().material.name}.")
    source.displayToMe(getRoutesString(source, location))
    source.displayToOthers("${source.name} looks around.")
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
        input.addAll(routes.map { getRouteString(source.thing, it) })
        val table = StringTable(input, 2, rightPadding = 2)

        "Options:\n${table.getString()}"
    } else {
        "There is nothing to climb to."
    }
}

private fun getRouteString(source: Thing, route: Route): List<String> {
    val exits = getExits(route.destination, source.climbThing!!)
    return listOf(route.destination.name, route.getDistance().toString(), route.getDirectionString(), "1", exits.joinToString(", "))
}

private fun getExits(location: LocationNode, climbThing: Thing): List<String> {
    val dismountLocation = if (climbThing.body.getClimbEntryParts().contains(location)) {
        climbThing.location.name
    } else {
        null
    }

    val things = climbThing.location.getNeighborConnections()
        .filter { (it.source.thingName == climbThing.name && it.source.partName == location.name) }
        .map { it.destination.location.name }

    return (things + dismountLocation).filterNotNull()
}