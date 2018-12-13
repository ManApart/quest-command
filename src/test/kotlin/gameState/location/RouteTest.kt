package gameState.location

import core.gameState.location.LocationLink
import core.gameState.location.LocationNode
import core.gameState.location.Route
import org.junit.Test
import kotlin.test.assertEquals

class RouteTest {

    @Test
    fun addLink() {
        val source = LocationNode("source")
        val destination = LocationNode("destination")
        val route = Route(source)
        route.addLink(LocationLink(source, destination))

        assertEquals(1, route.getLinks().size)
        assertEquals(source, route.source)
        assertEquals(destination, route.destination)
    }

    @Test(expected = IllegalArgumentException::class)
    fun routeDoesNotAcceptLinksOutOfOrder() {
        val source = LocationNode("source")
        val destination = LocationNode("destination")
        val route = Route(source)
        route.addLink(LocationLink(destination, source))
    }
}