package gameState.location

import org.junit.Test
import traveling.location.RouteFinder
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RouteFinderTest {

    @Test
    fun findRouteDepth1() {
        val source = LocationHelper().createLocations(1)
        val destination = source.getNeighborConnections().last().destination.location
        val routeFinder = RouteFinder(source, destination, 1)

        assertTrue(routeFinder.hasRoute())
        val route = routeFinder.getRoute()

        assertEquals(source, route.source)
        assertEquals(destination, route.destination)
        assertEquals(1, route.getConnections().size)
        assertEquals(1, route.getDistance())
    }

    @Test
    fun findRouteDepth2() {
        val source = LocationHelper().createLocations(2)
        val destination = source.getNeighborConnections().last().destination.location.getNeighborConnections().last().destination.location
        val routeFinder = RouteFinder(source, destination, 2)

        assertTrue(routeFinder.hasRoute())
        val route = routeFinder.getRoute()

        assertEquals(source, route.source)
        assertEquals(destination, route.destination)
        assertEquals(2, route.getConnections().size)
        assertEquals(2, route.getDistance())
    }



}