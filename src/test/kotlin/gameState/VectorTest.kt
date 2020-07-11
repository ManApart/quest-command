package gameState

import org.junit.Test
import traveling.direction.Direction
import traveling.position.Vector
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VectorTest {
    private val center = Vector()
    private val north = Vector(0, 1)
    private val west = Vector(-1)
    private val east = Vector(1)
    private val south = Vector(0, -1)
    private val above = Vector(0, 0, 1)
    private val below = Vector(0, 0, -1)
    private val northWest = Vector(-1, 1)
    private val northEast = Vector(1, 1)
    private val southWest = Vector(-1, -1)
    private val southEast = Vector(1, -1)

    @Test
    fun distanceIsCorrect() {
        val pos1 = Vector()
        val pos2 = Vector(5, 5, 5)

        assertEquals(pos1.getDistance(pos2), pos1.getDistance(pos2))
    }

    @Test
    fun directions() {
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
    fun directionsAreBasedOnLargestDistance() {
        val center = Vector()
        val northEast = Vector(7, 10)
        val northWest = Vector(-7, 10)
        val below = Vector(7, 9, -20)

        assertEquals(Direction.NORTH_EAST, center.calculateDirection(northEast))
        assertEquals(Direction.NORTH_WEST, center.calculateDirection(northWest))
        assertEquals(Direction.BELOW, center.calculateDirection(below))
    }

    @Test
    fun isInGeneralDirection() {
        assertTrue(north.isInGeneralDirection(Direction.NORTH))
        assertTrue(center.isInGeneralDirection(Direction.SOUTH, north))

        assertTrue(north.isInGeneralDirection(Direction.NORTH, center))
        assertTrue(northEast.isInGeneralDirection(Direction.NORTH, center))
        assertTrue(northWest.isInGeneralDirection(Direction.NORTH, center))

        assertTrue(northWest.isInGeneralDirection(Direction.WEST, center))
        assertTrue(west.isInGeneralDirection(Direction.WEST, center))
        assertTrue(southWest.isInGeneralDirection(Direction.WEST, center))

        assertFalse(south.isInGeneralDirection(Direction.NORTH, center))
        assertFalse(west.isInGeneralDirection(Direction.NORTH, center))
        assertFalse(east.isInGeneralDirection(Direction.NORTH, center))

        assertFalse(north.isInGeneralDirection(Direction.NORTH_WEST, center))
    }

    @Test
    fun inDirection50Percent() {
        val result = Vector().getVectorInDirection(Vector(10), 5)
        assertEquals(Vector(5), result)
    }

    @Test
    fun inDirection100Percent() {
        val source = Vector()
        val target = Vector(10, 10, 10)
        val distance = source.getDistance(target)
        val result = source.getVectorInDirection(target, distance)
        assertEquals(target, result)
    }

    @Test
    fun inDirection200Percent() {
        val source = Vector()
        val target = Vector(10, 10, 10)
        val further = Vector(20, 20, 20)
        val distance = source.getDistance(further)
        val result = source.getVectorInDirection(target, distance)
        assertEquals(further, result)
    }

    @Test
    fun inverseOfOtherSimple() {
        val result = Vector().getInverse(Vector(10))
        assertEquals(Vector(-10), result)
    }

    @Test
    fun inverseOfOther() {
        val source = Vector()
        val target = Vector(10, 10, 10)
        val result = source.getInverse(target)
        assertEquals(target.invert(), result)
    }

    @Test
    fun inverseOfOther2() {
        val source = Vector(10, 10, 10)
        val target = Vector()
        val result = source.getInverse(target)
        assertEquals(Vector(20, 20, 20), result)
    }

    @Test
    fun closerWithSameStart() {
        val source = Vector()
        assertEquals(source, source.closer(source, 5))
        assertEquals(source, source.closer(source, 10))
        assertEquals(source, source.closer(source, -105))
    }

    @Test
    fun furtherWithSameStartDefaultsNorth() {
        val source = Vector()
        assertEquals(Vector(y = 5), source.further(source, 5))
        assertEquals(Vector(y = 10), source.further(source, 10))
        assertEquals(Vector(y = -105), source.further(source, -105))
    }

    @Test
    fun closerIsSourcePlusDistanceTowardTarget() {
        val source = Vector(y = 0)
        val direction  = Vector(y = 10)
        val expected = Vector(y = 5)
        val actual = source.closer(direction, 5)
        assertEquals(expected, actual)
    }

    @Test
    fun closerPlusGreatDistanceStopsAtSamePlace() {
        val source = Vector(y = 0)
        val direction  = Vector(y = 10)
        val actual = source.closer(direction, 50)
        assertEquals(direction, actual)
    }

    @Test
    fun furtherIsTargetPlusAdditionalDistanceInSameDirectionFromSource() {
        val source = Vector(y = 0)
        val direction  = Vector(y = 10)
        val expected = Vector(y = 15)
        val actual = source.further(direction, 5)
        assertEquals(expected, actual)
    }

    @Test
    fun furtherWest() {
        val source = Vector(0)
        val direction  = Vector(10)
        val expected = Vector(15)
        val actual = source.further(direction, 5)
        assertEquals(expected, actual)
    }

    @Test
    fun furtherAlongSameDirection() {
        val furtherNorth = Vector(0, 10)
        val furtherWest = Vector(-10)
        val furtherSouthEast = Vector(10, -10)

        assertTrue(north.isFurtherAlongSameDirectionThan(center))
        assertTrue(furtherNorth.isFurtherAlongSameDirectionThan(north))
        assertTrue(furtherWest.isFurtherAlongSameDirectionThan(center))
        assertTrue(furtherWest.isFurtherAlongSameDirectionThan(west))
        assertTrue(furtherSouthEast.isFurtherAlongSameDirectionThan(center))
        assertTrue(furtherSouthEast.isFurtherAlongSameDirectionThan(southEast))
        assertTrue(furtherSouthEast.isFurtherAlongSameDirectionThan(south))

        assertFalse(north.isFurtherAlongSameDirectionThan(south))
        assertFalse(north.isFurtherAlongSameDirectionThan(furtherNorth))
        assertFalse(east.isFurtherAlongSameDirectionThan(west))
        assertFalse(west.isFurtherAlongSameDirectionThan(east))
        assertFalse(furtherSouthEast.isFurtherAlongSameDirectionThan(southWest))

    }

}