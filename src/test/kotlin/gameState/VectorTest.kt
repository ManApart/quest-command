package gameState

import core.gameState.Direction
import core.gameState.Vector
import org.junit.Test
import kotlin.test.assertEquals

class VectorTest {

    @Test
    fun distanceIsCorrect(){
        val pos1 = Vector()
        val pos2 = Vector(5,5,5)

        assertEquals(pos1.getDistance(pos2), pos1.getDistance(pos2))
    }

    @Test
    fun directions(){
        val center = Vector()
        val north = Vector(0, 1)
        val west = Vector(-1 )
        val east = Vector(1 )
        val south = Vector(0, -1)
        val above = Vector(0, 0, 1)
        val below = Vector(0, 0, -1)
        val northWest = Vector(-1, 1)
        val northEast = Vector(1, 1)
        val southWest = Vector(-1, -1)
        val southEast = Vector(1, -1)

        assertEquals(Direction.NORTH, center.calculateDirection(north))
        assertEquals(north, Direction.NORTH.vector)

        assertEquals(Direction.WEST, center.calculateDirection(west))
        assertEquals(west, Direction.WEST.vector)

        assertEquals(Direction.EAST, center.calculateDirection(east))
        assertEquals(east, Direction.EAST.vector)

        assertEquals(Direction.SOUTH, center.calculateDirection(south))
        assertEquals(south, Direction.SOUTH.vector)

        assertEquals(Direction.ABOVE, center.calculateDirection(above))
        assertEquals(above, Direction.ABOVE.vector)

        assertEquals(Direction.BELOW, center.calculateDirection(below))
        assertEquals(below, Direction.BELOW.vector)

        assertEquals(Direction.NORTH_WEST, center.calculateDirection(northWest))
        assertEquals(northWest, Direction.NORTH_WEST.vector)

        assertEquals(Direction.NORTH_EAST, center.calculateDirection(northEast))
        assertEquals(northEast, Direction.NORTH_EAST.vector)

        assertEquals(Direction.SOUTH_WEST, center.calculateDirection(southWest))
        assertEquals(southWest, Direction.SOUTH_WEST.vector)

        assertEquals(Direction.SOUTH_EAST, center.calculateDirection(southEast))
        assertEquals(southEast, Direction.SOUTH_EAST.vector)

        assertEquals(Direction.NONE, center.calculateDirection(center))
        assertEquals(center, Direction.NONE.vector)
    }

    @Test
    fun directionsAreBasedOnLargestDistance(){
        val center = Vector()
        val northEast = Vector(7, 10)
        val northWest = Vector(-7, 10)
        val below = Vector(7, 9, -20)

        assertEquals(Direction.NORTH_EAST, center.calculateDirection(northEast))
        assertEquals(Direction.NORTH_WEST, center.calculateDirection(northWest))
        assertEquals(Direction.BELOW, center.calculateDirection(below))
    }
}