package gameState.location

import core.gameState.Direction
import core.gameState.Vector
import core.gameState.location.Connection
import core.gameState.location.LocationNode
import core.gameState.location.LocationPoint
import core.gameState.location.RouteNeighborFinder
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class RouteNeighborFinderTest {

    @Test
    fun findNeighborsDepth1DoesNotIncludeOriginal() {
        val source = createLocations(1)
        val routeFinder = RouteNeighborFinder(source, 1)
        val neighbors = routeFinder.getNeighbors()

        assertEquals(4, neighbors.size)

        val destinations = neighbors.asSequence().map { it.destination }.toHashSet()
        assertEquals(4, destinations.size)
        assertFalse(destinations.contains(source))

        neighbors.forEach {
            assertEquals(1, it.getLinks().size)
            assertEquals(1, it.getDistance())
            assertEquals(source, it.source)
        }

    }

    @Test
    fun findNeighborsDepth2DoesNotIncludeDuplicates() {
        val source = createLocations(2)
        val routeFinder = RouteNeighborFinder(source, 2)
        val neighbors = routeFinder.getNeighbors()

        val destinations = neighbors.asSequence().map { it.destination }.toHashSet()
        assertEquals(8, destinations.size)
        assertFalse(destinations.contains(source))

        assertEquals(8, neighbors.size)

        neighbors.subList(4, neighbors.size).forEach {
            assertEquals(2, it.getLinks().size)
            assertEquals(2, it.getDistance())
            assertEquals(source, it.source)
        }
    }

    private fun createLocations(depth: Int): LocationNode {
        val source = LocationNode("Source")

        listOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)
                .forEach { direction ->
                    val neighbor = LocationNode(direction.toString())
                    val link = Connection(LocationPoint( source), LocationPoint(neighbor), Vector.fromDirection(direction))
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
        val neighbor = LocationNode(direction.toString() + (totalDepth -depth))
        val link = Connection(LocationPoint(source), LocationPoint(neighbor), Vector.fromDirection(direction))
        source.addLink(link)
        neighbor.addLink(link.invert())
        createLocations(neighbor, direction, depth - 1, totalDepth)
    }


}