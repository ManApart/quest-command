package gameState.location

import core.gameState.Direction
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NetworkTest {


    //TODO - eventually test with multiple nodes at the furthest point (expected count)
    @Test
    fun furthestLocationBelow() {
        assertFurthestNeighbors(Direction.ABOVE, 2, 1)
        assertFurthestNeighbors(Direction.BELOW, 3, 1)
        assertFurthestNeighbors(Direction.NORTH, 1, 1)
        assertFurthestNeighbors(Direction.SOUTH, 2, 1)
    }

    private fun assertFurthestNeighbors(direction: Direction, depth: Int, expectedCount: Int) {
        val network = LocationHelper().createNetwork(depth)
        val furthest = network.getFurthestLocations(direction)
        val addedInt = if (depth > 1) {(depth-1).toString()} else {""}
        val expectedName = direction.name + addedInt

        assertEquals(expectedCount, furthest.size)
        assertNotNull(furthest.firstOrNull{it.name ==expectedName}, "$expectedName was not found in ${furthest.joinToString { it.name }}")
    }

}