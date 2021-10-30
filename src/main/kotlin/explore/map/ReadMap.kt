package explore.map

import core.GameState
import core.events.EventListener
import core.history.StringTable
import core.history.displayToMe
import system.debug.DebugType
import traveling.location.RouteNeighborFinder

class ReadMap : EventListener<ReadMapEvent>() {
    override fun execute(event: ReadMapEvent) {
        if (event.source.location == event.thing) {
            event.source.displayToMe("You are in at ${event.source.position} in ${event.thing.name}.")
        }
        val name = "${event.thing.name} is a part of ${event.thing.parent}. It"
        event.source.displayToMe("$name ${getRoutesString(event)}")
    }

    private fun getRoutesString(event: ReadMapEvent): String {
        val ignoreHidden = !GameState.getDebugBoolean(DebugType.MAP_SHOW_ALL_LOCATIONS)
        val routes = RouteNeighborFinder(event.thing, event.depth, ignoreHidden, ignoreHidden, event.source).getNeighbors()

        return if (routes.isNotEmpty()) {
            val input = mutableListOf(listOf("Name", "Distance", "Direction Path"))
            input.addAll(routes.map { it.getRouteString() })
            val table = StringTable(input, 2, rightPadding = 2)

            "is neighbored by:\n${table.getString()}"
        } else {
            "has no known neighbors."
        }
    }

}