package gameState.location

import org.junit.Test
import traveling.location.RouteNeighborFinder
import traveling.location.location.LocationHelper
import traveling.location.network.LocationNode
import traveling.location.network.NetworkBuilder
import traveling.location.network.network
import traveling.position.Vector
import kotlin.test.assertEquals

class RouteVectorTest {

    @Test
    fun routeWithOriginStart() {
        val network = network("Network") {
            locationNode("A") {
                connection("B", x = 100)
            }

            locationNode("B") {
                connection("C", y = 10)
            }
            locationNode("C")
        }.buildRoutes()

        val locA = network.first { it.name == "A" }
        val locB = network.first { it.name == "B" }
        val locC = network.first { it.name == "C" }

        val route = RouteNeighborFinder(locA, 10).getNeighbors().first { it.destination == locC }
        val midRoute = RouteNeighborFinder(locB, 10).getNeighbors().first { it.destination == locC }

        assertEquals(2, route.getConnections().size)
        assertEquals(110, route.getDistance())
        assertEquals(Vector(100, 10), route.getVectorDistance())

        assertEquals(1, midRoute.getConnections().size)
        assertEquals(10, midRoute.getDistance())
        assertEquals(Vector(0, 10), midRoute.getVectorDistance())
    }

    @Test
    fun routeReversed() {
        val network = network("Network") {
            locationNode("A") {
                connection("B", x = 100)
            }

            locationNode("B") {
                connection("C", y = 10)
            }
            locationNode("C")
        }.buildRoutes()

        val locA = network.first { it.name == "A" }
        val locB = network.first { it.name == "B" }
        val locC = network.first { it.name == "C" }

        val route = RouteNeighborFinder(locC, 10).getNeighbors().first { it.destination == locA }
        val midRoute = RouteNeighborFinder(locB, 10).getNeighbors().first { it.destination == locA }


        assertEquals(2, route.getConnections().size)
        assertEquals(110, route.getDistance())
        assertEquals(Vector(-100, -10), route.getVectorDistance())

        assertEquals(1, midRoute.getConnections().size)
        assertEquals(100, midRoute.getDistance())
        assertEquals(Vector(-100), midRoute.getVectorDistance())
    }

    @Test
    fun routeWithOffsetStart() {
        val network = network("Network") {
            locationNode("A") {
                connection("B", x = 100)
            }

            locationNode("B") {
                connection("C", y = 10)
            }
            locationNode("C")
        }.buildRoutes()

        val locA = network.first { it.name == "A" }
        val locC = network.first { it.name == "C" }

        val route = RouteNeighborFinder(locA, 10).getNeighbors().first { it.destination == locC }
        val sourcePosition = Vector(50)

        assertEquals(2, route.getConnections().size)
        assertEquals(60, route.getDistance(sourcePosition))
        assertEquals(Vector(50, 10), route.getVectorDistance(sourcePosition))
    }

    private fun NetworkBuilder.buildRoutes(): List<LocationNode> {
        return build().also { nodes ->
            with(LocationHelper()) {
                createNeighborsAndNeighborLinks(buildInitialMap(nodes))
            }
        }
    }
}