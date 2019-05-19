package gameState.location

import core.gameState.Direction
import core.gameState.location.LocationNode
import org.junit.Test
import kotlin.test.assertEquals

class LocationNodeTest {

    @Test
    fun getNeighborsInDirection() {
        val source = LocationHelper().createLocations(1)

        assertNeighbors(source, Direction.NORTH)
        assertNeighbors(source, Direction.SOUTH)
        assertNeighbors(source, Direction.ABOVE)
        assertNeighbors(source, Direction.BELOW)
    }

    private fun assertNeighbors(source: LocationNode, direction: Direction) {
        val locations = source.getNeighbors(direction)
        assertEquals(1, locations.size, "$direction had ${locations.size} instead of 1.")
        assertEquals(direction.name, locations.first().name)
    }
}