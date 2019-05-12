package gameState.location

import core.gameState.Direction
import core.gameState.Position
import core.gameState.location.Connection
import core.gameState.location.LocationNode
import core.gameState.location.LocationPoint
import core.gameState.location.RouteFinder
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RouteFinderTest {

    @Test
    fun findRouteDepth1() {
        val source = createLocations(1)
        val destination = source.getNeighborLinks().last().destination.location
        val routeFinder = RouteFinder(source, destination, 1)

        assertTrue(routeFinder.hasRoute())
        val route = routeFinder.getRoute()

        assertEquals(source, route.source)
        assertEquals(destination, route.destination)
        assertEquals(1, route.getLinks().size)
        assertEquals(1, route.getDistance())
    }

    @Test
    fun findRouteDepth2() {
        val source = createLocations(2)
        val destination = source.getNeighborLinks().last().destination.location.getNeighborLinks().last().destination.location
        val routeFinder = RouteFinder(source, destination, 2)

        assertTrue(routeFinder.hasRoute())
        val route = routeFinder.getRoute()

        assertEquals(source, route.source)
        assertEquals(destination, route.destination)
        assertEquals(2, route.getLinks().size)
        assertEquals(2, route.getDistance())
    }

    private fun createLocations(depth: Int): LocationNode {
        val source = LocationNode("Source")

        listOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)
                .forEach { direction ->
                    val neighbor = LocationNode(direction.toString())
                    val link = Connection(LocationPoint( source), LocationPoint(neighbor), Position.fromDirection(direction))
                    source.addLink(link)
                    neighbor.addLink(link.invert())
                    createLocations(neighbor, direction, depth - 1, depth)
                }

        return source
    }

    private fun createLocations(source: LocationNode, direction: Direction, depth: Int, totalDepth: Int) {
        if (depth <= 0) {
            return
        }
        val neighbor = LocationNode(direction.toString() + (totalDepth-depth))
        val link = Connection(LocationPoint(source), LocationPoint(neighbor), Position.fromDirection(direction))
        source.addLink(link)
        neighbor.addLink(link.invert())
        createLocations(neighbor, direction, depth - 1, totalDepth)
    }


}