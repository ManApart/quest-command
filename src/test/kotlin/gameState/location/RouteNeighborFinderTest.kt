package gameState.location

import org.junit.Test
import traveling.location.RouteNeighborFinder
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class RouteNeighborFinderTest {

    @Test
    fun findNeighborsDepth1DoesNotIncludeOriginal() {
        val source = LocationHelper().createLocations(1)
        val routeFinder = RouteNeighborFinder(source, 1)
        val neighbors = routeFinder.getNeighbors()

        assertEquals(6, neighbors.size)

        val destinations = neighbors.asSequence().map { it.destination }.toHashSet()
        assertEquals(6, destinations.size)
        assertFalse(destinations.contains(source))

        neighbors.forEach {
            assertEquals(1, it.getConnections().size)
            assertEquals(1, it.getDistance())
            assertEquals(source, it.source)
        }

    }

    @Test
    fun findNeighborsDepth2DoesNotIncludeDuplicates() {
        val source = LocationHelper().createLocations(2)
        val routeFinder = RouteNeighborFinder(source, 2)
        val neighbors = routeFinder.getNeighbors()

        val destinations = neighbors.asSequence().map { it.destination }.toHashSet()
        assertEquals(12, destinations.size)
        assertFalse(destinations.contains(source))

        assertEquals(12, neighbors.size)

        neighbors.subList(6, neighbors.size).forEach {
            assertEquals(2, it.getConnections().size)
            assertEquals(2, it.getDistance())
            assertEquals(source, it.source)
        }
    }




}