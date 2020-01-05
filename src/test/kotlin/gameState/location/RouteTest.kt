package gameState.location

import traveling.direction.Direction
import org.junit.Test
import traveling.location.Connection
import traveling.location.location.LocationNode
import traveling.location.location.LocationPoint
import traveling.location.Route
import kotlin.test.assertEquals

class RouteTest {

    @Test
    fun addLink() {
        val source = LocationNode("source")
        val destination = LocationNode("destination")
        val route = Route(source)
        route.addLink(Connection(LocationPoint(source), LocationPoint(destination)))

        assertEquals(1, route.getConnections().size)
        assertEquals(source, route.source)
        assertEquals(destination, route.destination)
    }

    @Test(expected = IllegalArgumentException::class)
    fun routeDoesNotAcceptLinksOutOfOrder() {
        val source = LocationNode("source")
        val destination = LocationNode("destination")
        val route = Route(source)
        route.addLink(Connection(LocationPoint(destination), LocationPoint(source)))
    }

    @Test
    fun routeDirectionString() {
        val route = Route(LocationNode("source"))
        val directions = listOf(Direction.NORTH, Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.SOUTH_EAST)

        directions.forEach {
            route.addLink(Connection(LocationPoint(route.destination), LocationPoint(LocationNode(it.name)), it.vector))
        }

        assertEquals("N, NE, NW, E, W, S, SW, SE", route.getDirectionString())
    }

}