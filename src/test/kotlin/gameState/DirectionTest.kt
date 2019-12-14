package gameState

import org.junit.Test
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

}