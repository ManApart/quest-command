package gameState

import core.gameState.Direction
import core.gameState.Position
import org.junit.Assert
import org.junit.Test

class PositionTest {

    @Test
    fun distanceIsCorrect(){
        val pos1 = Position()
        val pos2 = Position(5,5,5)

        Assert.assertEquals(pos1.getDistance(pos2), pos1.getDistance(pos2))
    }

    @Test
    fun directions(){
        val center = Position()
        val north = Position(0, 10)
        val west = Position(-10 )
        val east = Position(10 )
        val south = Position(0, -10)
        val above = Position(0, 0, 10)
        val below = Position(0, 0, -10)
        val northWest = Position(-10, 10)
        val northEast = Position(10, 10)
        val southWest = Position(-10, -10)
        val southEast = Position(10, -10)

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
        val center = Position()
        val northEast = Position(7, 10)
        val northWest = Position(-7, 10)
        val below = Position(7, 9, -20)

        Assert.assertEquals(Direction.NORTH_EAST, center.getDirection(northEast))
        Assert.assertEquals(Direction.NORTH_WEST, center.getDirection(northWest))
        Assert.assertEquals(Direction.BELOW, center.getDirection(below))
    }
}