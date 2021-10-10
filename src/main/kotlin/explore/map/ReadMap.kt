package explore.map

import core.GameState
import core.events.EventListener
import core.history.StringTable
import core.history.displayToMe
import system.debug.DebugType
import traveling.location.Route
import traveling.location.RouteNeighborFinder

class ReadMap : EventListener<ReadMapEvent>() {
    override fun execute(event: ReadMapEvent) {
        if (event.source.location == event.target) {
            event.source.displayToMe("You are in at ${event.source.position} in ${event.target.name}.")
        }
        val name = "${event.target.name} is a part of ${event.target.parent}. It"
        event.source.displayToMe("$name ${getRoutesString(event)}")
    }

    private fun getRoutesString(event: ReadMapEvent): String {
        val ignoreHidden = !GameState.getDebugBoolean(DebugType.MAP_SHOW_ALL_LOCATIONS)
        val routes = RouteNeighborFinder(event.target, event.depth, ignoreHidden, ignoreHidden).getNeighbors()

        return if (routes.isNotEmpty()) {
            val input = mutableListOf(listOf("Name", "Distance", "Direction Path"))
            input.addAll(routes.map { getRouteString(it) })
            val table = StringTable(input, 2, rightPadding = 2)

            "is neighbored by:\n${table.getString()}"
        } else {
            "has no known neighbors."
        }
    }

    private fun getRouteString(route: Route): List<String> {
        return listOf(route.destination.name, route.getDistance().toString(), route.getDirectionString())
    }

}