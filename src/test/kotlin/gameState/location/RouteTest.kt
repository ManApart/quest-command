package gameState.location

import org.junit.Test
import traveling.direction.Direction
import traveling.location.Connection
import traveling.location.Route
import traveling.location.location.LocationPoint
import traveling.location.network.LocationNode
import kotlin.test.assertEquals
import kotlin.test.assertNull

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

    @Test
    fun trimRoute() {
        val route = Route(LocationNode("source"))
        val directions = listOf(Direction.NORTH, Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.SOUTH_EAST)

        directions.forEach {
            route.addLink(Connection(LocationPoint(route.destination), LocationPoint(LocationNode(it.name)), it.vector))
        }

        val newStart = route.getConnections().first { it.originPoint.direction.shortcut == "e" }.source.location
        val trimmed = route.trim(newStart)

        assertEquals("E, W, S, SW, SE", trimmed?.getDirectionString())
        assertEquals(newStart, trimmed?.source)
        assertEquals(directions.last().name, trimmed?.destination?.name)
    }

    @Test
    fun trimRouteIdempotent() {
        val route = Route(LocationNode("source"))
        val directions = listOf(Direction.NORTH, Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.SOUTH_EAST)

        directions.forEach {
            route.addLink(Connection(LocationPoint(route.destination), LocationPoint(LocationNode(it.name)), it.vector))
        }

        val newStart = route.getConnections().first { it.originPoint.direction.shortcut == "e" }.source.location
        val trimmed = route.trim(newStart)?.trim(newStart)

        assertEquals("E, W, S, SW, SE", trimmed?.getDirectionString())
    }

    @Test
    fun trimRouteEdgeCase() {
        val route = Route(LocationNode("source"))
        val directions = listOf(Direction.NORTH, Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.SOUTH_EAST)

        directions.forEach {
            route.addLink(Connection(LocationPoint(route.destination), LocationPoint(LocationNode(it.name)), it.vector))
        }

        val newStart = route.destination
        val trimmed = route.trim(newStart)

        assertNull(trimmed)
    }

}