package traveling.position

import core.utility.clamp

class Shape(val vertices: List<Vector> = listOf()) {
    val min = calcMins()
    val max = calcMaxs()

    override fun toString(): String {
        if (min == NO_VECTOR && max == NO_VECTOR) return ""

        val zString = if (min.z != 0 || max.z != 0) " and Z: ${min.z} to ${max.z}" else ""

        return "X: ${min.x} to ${max.x} and Y: ${min.y} to ${max.y}" + zString
    }

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