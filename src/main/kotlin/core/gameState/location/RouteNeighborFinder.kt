package core.gameState.location

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

    private fun findNeighbors() {
        source.getNeighborLinks().forEach {
            val route = Route(source)
            route.addLink(it)
            neighbors.add(route)
            potentials.add(route)
        }
        examined.add(source)

        buildNeighbors()
    }

    private fun buildNeighbors() {
        if (potentials.isNotEmpty() && depth > potentials.first().getLinks().size) {
            val current = potentials.toList()
            potentials.clear()

            current.forEach { route ->
                if (!examined.contains(route.destination)) {
                    examined.add(route.destination)
                    route.destination.getNeighborLinks().forEach { connection ->
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