package gameState



import traveling.direction.Direction
import kotlin.test.assertEquals

class DirectionTest {

    @Test
    fun directionInverted() {
        assertEquals(Direction.BELOW, Direction.ABOVE.invert())
        assertEquals(Direction.ABOVE, Direction.BELOW.invert())
        assertEquals(Direction.NORTH, Direction.SOUTH.invert())
        assertEquals(Direction.SOUTH_WEST, Direction.NORTH_EAST.invert())
        assertEquals(Direction.NONE, Direction.NONE.invert())
    }

    @Test
    fun directionString() {
        assertEquals("above", Direction.ABOVE.directionString())
        assertEquals("below", Direction.BELOW.directionString())
        assertEquals("to the south", Direction.SOUTH.directionString())
        assertEquals("to the north east", Direction.NORTH_EAST.directionString())
        assertEquals("", Direction.NONE.directionString())
    }

}