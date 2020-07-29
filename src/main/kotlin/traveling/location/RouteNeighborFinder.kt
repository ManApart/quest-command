package traveling.location

import traveling.location.location.LocationNode

class RouteNeighborFinder(private val source: LocationNode, private val depth: Int) {
    private val neighbors: MutableList<Route> = mutableListOf()
    private val potentials: MutableList<Route> = mutableListOf()
    private val examined: MutableList<LocationNode> = mutableListOf()

    init {
        findNeighbors()
    }

    fun getNeighbors(): List<Route> {
        return neighbors.toList()
    }

    fun getDestinations() : List<LocationNode> {
        return neighbors.map { it.destination }
    }

    private fun findNeighbors() {
        source.getNeighborConnections().forEach {
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
                    route.destination.getNeighborConnections().forEach { connection ->
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


}