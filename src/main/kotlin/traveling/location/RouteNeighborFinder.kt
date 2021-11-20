package traveling.location

import core.Player
import traveling.location.network.LocationNode

class RouteNeighborFinder(
    private val source: LocationNode,
    private val depth: Int,
    private val ignoreHiddenConnections: Boolean = false,
    private val ignoreUndiscoveredLocations: Boolean = false,
    private val player: Player? = null
) {
    private val neighbors: MutableList<Route> = mutableListOf()
    private val potentials: MutableList<Route> = mutableListOf()
    private val examined: MutableList<LocationNode> = mutableListOf()

    init {
        findNeighbors()
    }

    fun getNeighbors(): List<Route> {
        val neighborMap = getDestinations().associateWith { mutableListOf<Route>() }
        neighbors.forEach { route -> neighborMap[route.destination]?.add(route) }
        return neighborMap.values.mapNotNull { routes -> routes.minByOrNull { it.getDistance() } }.sortedBy { it.getDistance() }
    }

    fun getDestinations(): List<LocationNode> {
        return neighbors.map { it.destination }
    }

    private fun findNeighbors() {
        source.getFilteredNeighborConnections().forEach {
            val route = Route(source)
            route.addLink(it)
            neighbors.add(route)
            potentials.add(route)
        }
        examined.add(source)

        buildNeighbors()
    }
    private fun buildNeighbors() {
        if (potentials.isNotEmpty() && depth > potentials.first().getConnections().size) {
            val current = potentials.toList()
            potentials.clear()

            current.forEach { route ->
                if (!examined.contains(route.destination)) {
                    examined.add(route.destination)

                    route.destination.getFilteredNeighborConnections().forEach { connection ->
                        if (!examined.contains(connection.destination.location)) {
                            val newRoute = Route(route)
                            newRoute.addLink(connection)
                            neighbors.add(newRoute)
                            potentials.add(newRoute)
                        }
                    }
                }
            }
            buildNeighbors()
        }
    }

    private fun LocationNode.getFilteredNeighborConnections() : List<Connection>{
        return getNeighborConnections().let { connections ->
            if (ignoreHiddenConnections){
                connections.filter { !it.hidden }
            } else connections
        }.let { connections ->
            if (ignoreUndiscoveredLocations && player != null){
                connections.filter { player.knows(it.destination.location) }
            } else connections
        }
    }


}