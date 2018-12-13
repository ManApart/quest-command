package core.gameState.location

class RouteFinder(private val source: LocationNode, private val depth: Int) {
    private val neighbors: MutableList<Route> = mutableListOf()
    private val potentials: MutableList<Route> = mutableListOf()
    private val examined: MutableList<LocationNode> = mutableListOf()


    fun getNeighbors(): List<Route> {
        source.getNeighborLinks().forEach {
            val route = Route(source)
            route.addLink(it)
            neighbors.add(route)
            potentials.add(route)
        }
        examined.add(source)

        buildNeighbors()
        return neighbors
    }

    private fun buildNeighbors() {
        if (potentials.isNotEmpty() && depth > potentials.first().getLinks().size) {
            val current = potentials.toList()
            potentials.clear()

            current.forEach { route ->
                if (!examined.contains(route.destination)) {
                    examined.add(route.destination)
                    route.destination.getNeighborLinks().forEach { link ->
                        if (!examined.contains(link.destination)) {
                            val newRoute = Route(route)
                            newRoute.addLink(link)
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