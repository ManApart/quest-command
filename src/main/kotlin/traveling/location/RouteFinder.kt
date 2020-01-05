package traveling.location

import traveling.location.location.LocationNode

class RouteFinder(private val source: LocationNode, private val destination: LocationNode, private val depth: Int = 10) {
    private val potentials: MutableList<Route> = mutableListOf()
    private val examined: MutableList<LocationNode> = mutableListOf()
    private var solution: Route? = null

    init {
        findRoute()
    }

    fun hasRoute(): Boolean {
        return solution != null
    }

    fun getRoute(): Route {
        return solution!!
    }

    private fun findRoute() {
        if (source.getNeighborConnections().any { it.destination.location == destination }) {
            solution = Route(source)
            solution?.addLink(source.getNeighborConnections().first { it.destination.location == destination })
        } else {
            source.getNeighborConnections().forEach {
                val route = Route(source)
                route.addLink(it)
                potentials.add(route)
                if (route.destination == destination){
                    solution = route
                }
            }
            examined.add(source)

            buildNeighbors()
        }
    }


    private fun buildNeighbors() {
        if (solution == null && potentials.isNotEmpty() && depth > potentials.first().getConnections().size) {
            val current = potentials.toList()
            potentials.clear()

            current.forEach { route ->
                if (!examined.contains(route.destination)) {
                    examined.add(route.destination)
                    route.destination.getNeighborConnections().forEach { connection ->
                        if (!examined.contains(connection.destination.location) && solution == null) {
                            val newRoute = Route(route)
                            newRoute.addLink(connection)
                            potentials.add(newRoute)
                            if (newRoute.destination == destination){
                                solution = newRoute
                            }
                        }
                    }
                }
            }
            buildNeighbors()
        }
    }


}