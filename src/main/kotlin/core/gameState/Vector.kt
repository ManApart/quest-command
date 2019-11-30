package core.gameState

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

val NO_VECTOR: Vector = Vector()

class Vector(val x: Int = 0, val y: Int = 0, val z: Int = 0) {
    /**
     * The direction this vector is relative to 0.0.0
     * The same as calling (0.0.0).calculateDirection(this)
     */
    val direction: Direction by lazy { calculateDirection() }

    override fun toString(): String {
        return "$x, $y, $z"
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

    operator fun plus(other: Vector): Vector {
        return Vector(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Vector): Vector {
        return Vector(x - other.x, y - other.y, z - other.z)
    }

    operator fun times(magnitude: Int): Vector {
        return Vector(x * magnitude, y * magnitude, z * magnitude)
    }

    operator fun times(other: Vector): Vector {
        return Vector(x * other.x, y * other.y, z * other.z)
    }

    fun closer(other: Vector, amount: Int): Vector {
        return getVectorInDirection(other, amount)
    }

    fun further(other: Vector, amount: Int): Vector {
        if (this == NO_VECTOR && other == NO_VECTOR){
            return getVectorInDirection(Vector(y=1), amount)
        }
        return other + getVectorInDirection(other, amount)
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
        val zAbs = abs(zVal)
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
     * floor.isDirection(below, roof) == true
     */
    fun isDirection(direction: Direction, other: Vector): Boolean {
        return direction == other.calculateDirection(this)
    }

    fun isInGeneralDirection(direction: Direction, other: Vector = Vector()): Boolean {
        val diffVector = this - other
        return when {
            direction.vector.x != 0 && !sameSign(direction.vector.x, diffVector.x) -> false
            direction.vector.y != 0 && !sameSign(direction.vector.y, diffVector.y) -> false
            direction.vector.z != 0 && !sameSign(direction.vector.z, diffVector.z) -> false
            else -> true
        }

    }

    private fun sameSign(a: Int, b: Int): Boolean {
        return when {
            a == b -> true
            a > 0 && b > 0 -> true
            a < 0 && b < 0 -> true
            else -> false
        }
    }

    fun getDistance(other: Vector = Vector()): Int {
        val x = (x - other.x).toDouble().pow(2)
        val y = (y - other.y).toDouble().pow(2)
        val z = (z - other.z).toDouble().pow(2)
        return sqrt(x + y + z).toInt()
    }

    private fun getDistanceXY(other: Vector): Int {
        val x = (x - other.x).toDouble().pow(2)
        val y = (y - other.y).toDouble().pow(2)
        return sqrt(x + y).toInt()
    }

    fun getDistanceZ(other: Vector): Int {
        return abs(z - other.z)
    }

    /**
     * (0,0,0).getAngleXY(10,0,0) would return 90 degrees
     */
    private fun getAngleXY(other: Vector): Int {
        val dx = (other.x - x)
        val dy = (other.y - y)
        val rads = atan2(dy.toDouble(), dx.toDouble())
        val adjustedRads = if (rads < 0)
            abs(rads)
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

    fun getInverse(other: Vector): Vector{
        val difference = other - this
        return this - difference
    }

    fun getVectorInDirection(target: Vector, distance: Int): Vector {
        val percent = distance / getDistance(target).toFloat()
        val inversePercent = 1 - percent

        val newX = x * inversePercent + target.x * percent
        val newY = y * inversePercent + target.y * percent
        val newZ = z * inversePercent + target.z * percent

        return Vector(newX.toInt(), newY.toInt(), newZ.toInt())
    }

}

fun Sequence<Vector>.sum(): Vector {
    var sum = Vector()
    for (element in this) {
        sum += element
    }
    return sum
}