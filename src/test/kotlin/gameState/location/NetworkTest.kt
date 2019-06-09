package gameState.location

import core.gameState.Direction
import core.gameState.Vector
import core.gameState.location.Connection
import core.gameState.location.LocationNode
import core.gameState.location.LocationPoint
import core.gameState.location.Network
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
        assertNotNull(furthest.firstOrNull{it.name == expectedName}, "$expectedName was not found in ${furthest.joinToString { it.name }}")
    }


    @Test
    fun furthestNodeDiagonal() {
        val bottom = LocationNode("Bottom")
        val up = LocationNode("Up")
        val diagonalUp = LocationNode("DiagUp")
        val top = LocationNode("Top")

        val bottomUp = Connection(LocationPoint(bottom), LocationPoint(up), Vector(z=1))
        val buttomDiagUp =Connection(LocationPoint(bottom), LocationPoint(diagonalUp), Vector(x=1, z=1))
        val upTop = Connection(LocationPoint(up), LocationPoint(top), Vector(z=1))
        val diagUpTop = Connection(LocationPoint(diagonalUp), LocationPoint(top), Vector(z=1))

        bottom.addConnection(bottomUp)
        up.addConnection(bottomUp.invert())

        bottom.addConnection(buttomDiagUp)
        diagonalUp.addConnection(buttomDiagUp.invert())

        up.addConnection(upTop)
        top.addConnection(upTop.invert())

        diagonalUp.addConnection(diagUpTop)
        top.addConnection(diagUpTop.invert())

        val network = Network("Network", listOf(bottom, up, diagonalUp, top))

        val bottomNodes = network.getFurthestLocations(Direction.BELOW)
        assertEquals(1, bottomNodes.size)
        assertEquals(bottom, bottomNodes.first())

    }

}