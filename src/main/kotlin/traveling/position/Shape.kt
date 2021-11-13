package traveling.position

import core.utility.clamp
import kotlin.math.max
import kotlin.math.min

class Shape(val vertices: List<Vector> = listOf(), minRadius: Int = 0) {
    val min = calcMins(minRadius)
    val max = calcMaxs(minRadius)

    private fun calcMins(minRadius: Int): Vector {
        return Vector(vertices.boundedMin(minRadius) { it.x }, vertices.boundedMin(minRadius)  { it.y }, vertices.minOf  { it.z })
    }

    private fun calcMaxs(minRadius: Int): Vector {
        return Vector(vertices.boundedMax(minRadius) { it.x }, vertices.boundedMax(minRadius) { it.y }, vertices.minOf { it.z })
    }

    private fun List<Vector>.boundedMin(min: Int, transform: (Vector) -> Int): Int {
       return min(-min, minOf(transform))
    }

    private fun List<Vector>.boundedMax(min: Int, transform: (Vector) -> Int): Int {
       return max(min, maxOf(transform))
    }

    fun covers(point: Vector): Boolean {
        return point.x in min.x..max.x
                && point.y in min.y..max.y
                && point.z in min.z..max.z
    }

    /**
     * Trim this point to exist within the shape
     */
    fun trim(point: Vector): Vector {
        val newX = point.x.clamp(min.x, max.x)
        val newY = point.y.clamp(min.y, max.y)
        val newZ = point.z.clamp(min.z, max.z)
        return Vector(newX, newY, newZ)
    }
}