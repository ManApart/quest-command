package traveling.position

import core.utility.clamp

class Shape(val vertices: List<Vector> = listOf(), val minRadius: Int = 0) {
    val min = calcMins()
    val max = calcMaxs()

    private fun calcMins(): Vector {
        return Vector(vertices.minOf { it.x }, vertices.minOf { it.y }, vertices.minOf { it.z })
    }

    private fun calcMaxs(): Vector {
        return Vector(vertices.maxOf { it.x }, vertices.maxOf { it.y }, vertices.maxOf { it.z })
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