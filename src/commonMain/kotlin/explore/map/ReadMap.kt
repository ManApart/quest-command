package explore.map

import core.GameState
import core.events.EventListener
import core.history.StringTable
import core.history.displayToMe
import explore.listen.addSoundEffect
import system.debug.DebugType
import traveling.location.RouteNeighborFinder

class ReadMap : EventListener<ReadMapEvent>() {
    override suspend fun complete(event: ReadMapEvent) {
        if (event.source.location == event.thing) {
            event.source.displayToMe("You are at ${event.source.position} in ${event.thing.name}.")
            event.source.displayToMe("${event.thing.name} has bounds ${event.thing.getLocation().bounds}")
        }
        val name = "${event.thing.name} is a part of ${event.thing.parent}. It"
        event.source.displayToMe("$name ${getRoutesString(event)}")
        event.source.thing.addSoundEffect("Reading", "the soft rustle of paper", 1)
    }

    private fun getRoutesString(event: ReadMapEvent): String {
        val ignoreHidden = !GameState.getDebugBoolean(DebugType.MAP_SHOW_ALL_LOCATIONS)
        val routes = RouteNeighborFinder(event.thing, event.depth, ignoreHidden, ignoreHidden, event.source).getNeighbors()

        return if (routes.isNotEmpty()) {
            val input = mutableListOf(listOf("Name", "Distance", "Direction Path"))
            input.addAll(routes.map { it.getRouteString(event.source.position) })
            val table = StringTable(input, 2, rightPadding = 2)

            "is neighbored by:\n${table.getString()}"
        } else {
            "has no known neighbors."
        }
    }

}