package explore.map

import core.events.EventListener
import core.gameState.GameState
import core.gameState.location.Route
import core.gameState.location.RouteNeighborFinder
import core.history.StringTable
import core.history.display

class ReadMap : EventListener<ReadMapEvent>() {
    override fun execute(event: ReadMapEvent) {
        if (GameState.player.location == event.target) {
            display("You are in ${event.target.name}.")
        }
        val name = if (event.target.parent != null) {
            "${event.target.name} is a part of ${event.target.parent}. It"
        } else {
            event.target.name
        }

        display("$name ${getRoutesString(event)}")
    }

    private fun getRoutesString(event: ReadMapEvent) : String {
        val routes = RouteNeighborFinder(event.target, event.depth).getNeighbors()

        return if (routes.isNotEmpty()) {
            val input = mutableListOf(listOf("Name", "Distance", "Direction Path"))
            input.addAll(routes.map { getRouteString(it) })
            val table = StringTable(input, 2, rightPadding = 2)

            "is neighbored by:\n${table.getString()}"
        } else {
            "has no known neighbors."
        }
    }

    private fun getRouteString(route: Route) : List<String> {
        return listOf(route.destination.name, route.getDistance().toString(), route.getDirectionString())
    }

}