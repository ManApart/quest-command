package gameState

import core.gameState.Direction
import core.gameState.Vector
import org.junit.Assert
import org.junit.Test

class VectorTest {

    @Test
    fun distanceIsCorrect(){
        val pos1 = Vector()
        val pos2 = Vector(5,5,5)

        Assert.assertEquals(pos1.getDistance(pos2), pos1.getDistance(pos2))
    }

    @Test
    fun directions(){
        val center = Vector()
        val north = Vector(0, 10)
        val west = Vector(-10 )
        val east = Vector(10 )
        val south = Vector(0, -10)
        val above = Vector(0, 0, 10)
        val below = Vector(0, 0, -10)
        val northWest = Vector(-10, 10)
        val northEast = Vector(10, 10)
        val southWest = Vector(-10, -10)
        val southEast = Vector(10, -10)

        Assert.assertEquals(Direction.NORTH, center.getDirection(north))
        Assert.assertEquals(Direction.WEST, center.getDirection(west))
        Assert.assertEquals(Direction.EAST, center.getDirection(east))
        Assert.assertEquals(Direction.SOUTH, center.getDirection(south))
        Assert.assertEquals(Direction.ABOVE, center.getDirection(above))
        Assert.assertEquals(Direction.BELOW, center.getDirection(below))
        Assert.assertEquals(Direction.NORTH_WEST, center.getDirection(northWest))
        Assert.assertEquals(Direction.NORTH_EAST, center.getDirection(northEast))
        Assert.assertEquals(Direction.SOUTH_WEST, center.getDirection(southWest))
        Assert.assertEquals(Direction.SOUTH_EAST, center.getDirection(southEast))
        Assert.assertEquals(Direction.NONE, center.getDirection(center))
    }

    @Test
    fun directionsAreBasedOnLargestDistance(){
        val center = Vector()
        val northEast = Vector(7, 10)
        val northWest = Vector(-7, 10)
        val below = Vector(7, 9, -20)

        Assert.assertEquals(Direction.NORTH_EAST, center.getDirection(northEast))
        Assert.assertEquals(Direction.NORTH_WEST, center.getDirection(northWest))
        Assert.assertEquals(Direction.BELOW, center.getDirection(below))
    }
}