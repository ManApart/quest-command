package core.gameState

import kotlin.math.pow

class Position(val x: Int = 0, val y: Int = 0, val z: Int = 0) {

    override fun toString(): String {
        return "Pos: $x, $y, $z"
    }

    /**
     * Return the direction to go in order to arrive at the other position
     * (0,0,0).getDirection(10,0,0) would return EAST
     */
    fun getDirection(other: Position): Direction {
        val zVal = z - other.z
        val zAbs = Math.abs(zVal)
        val xyDist = getDistanceXY(other)
        val angle = getAngleXY(other)

//        println("angle between $this and $other = $angle")

        return when {
            x == other.x && y == other.y && z == other.z -> Direction.NONE
            zAbs > xyDist && zVal < 0 -> Direction.ABOVE
            zAbs > xyDist && zVal > 0 -> Direction.BELOW
            angle in 22..67 -> Direction.NORTH_EAST
            angle in 67..112 -> Direction.EAST
            angle in 112..157 -> Direction.SOUTH_EAST
            angle in 157..202 -> Direction.SOUTH
            angle in 202..247 -> Direction.SOUTH_WEST
            angle in 247..292 -> Direction.WEST
            angle in 292..337 -> Direction.NORTH_WEST
            angle >= 337 || angle < 22 -> Direction.NORTH
            else -> Direction.NONE
        }
    }

    fun getDistance(other: Position): Int {
        val x = (x - other.x).toDouble().pow(2)
        val y = (y - other.y).toDouble().pow(2)
        val z = (z - other.z).toDouble().pow(2)
        return Math.sqrt(x + y + z).toInt()
    }

    private fun getDistanceXY(other: Position): Int {
        val x = (x - other.x).toDouble().pow(2)
        val y = (y - other.y).toDouble().pow(2)
        return Math.sqrt(x + y).toInt()
    }

    /**
     * (0,0,0).getAngleXY(10,0,0) would return 90 degrees
     */
    private fun getAngleXY(other: Position): Int {
        val dx = (other.x-x)
        val dy = (other.y - y)
        val rads = Math.atan2(dy.toDouble(), dx.toDouble())
        val adjustedRads = if (rads < 0)
            Math.abs(rads)
        else
            2 * Math.PI - rads

        val degrees = Math.toDegrees(adjustedRads).toInt()
        return if (degrees >= 270) {
            degrees - 270
        } else {
            degrees + 90
        }
    }


}