package core.gameState

import kotlin.math.pow

val NO_VECTOR: Vector = Vector()

class Vector(val x: Int = 0, private val y: Int = 0, val z: Int = 0) {
    /**
     * The direction this vector is relative to 0.0.0
     * The same as calling (0.0.0).calculateDirection(this)
     */
    val direction: Direction by lazy { calculateDirection() }

    override fun toString(): String {
        return "Pos: $x, $y, $z"
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Vector) {
            x == other.x && y == other.y && z == other.z
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }


    fun add(other: Vector): Vector {
        return Vector(x + other.x, y + other.y, z + other.z)
    }

    private fun calculateDirection(): Direction {
        return NO_VECTOR.calculateDirection(this)
    }

    /**
     * Return the direction to go in order to arrive at the other vector
     * (0,0,0).calculateDirection(10,0,0) would return EAST
     */
    fun calculateDirection(other: Vector): Direction {
        val zVal = z - other.z
        val zAbs = Math.abs(zVal)
        val xyDist = getDistanceXY(other)
        val angle = getAngleXY(other)

//        display("angle between $this and $other = $angle")

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

    /**
     * Floor.isDirection(below, roof) == true
     */
    fun isDirection(direction: Direction, other: Vector): Boolean {
        return direction == other.calculateDirection(this)
    }

    fun getDistance(other: Vector = Vector()): Int {
        val x = (x - other.x).toDouble().pow(2)
        val y = (y - other.y).toDouble().pow(2)
        val z = (z - other.z).toDouble().pow(2)
        return Math.sqrt(x + y + z).toInt()
    }

    private fun getDistanceXY(other: Vector): Int {
        val x = (x - other.x).toDouble().pow(2)
        val y = (y - other.y).toDouble().pow(2)
        return Math.sqrt(x + y).toInt()
    }

    fun getDistanceZ(other: Vector): Int {
        return Math.abs(z - other.z)
    }

    /**
     * (0,0,0).getAngleXY(10,0,0) would return 90 degrees
     */
    private fun getAngleXY(other: Vector): Int {
        val dx = (other.x - x)
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

    fun invert(): Vector {
        return Vector(-x, -y, -z)
    }

}