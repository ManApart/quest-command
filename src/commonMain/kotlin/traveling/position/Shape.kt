package traveling.position

import core.utility.clamp

class Shape(val vertices: List<Vector> = listOf()) {
    val min = calcMins()
    val max = calcMaxs()

    override fun toString(): String {
        if (min == NO_VECTOR && max == NO_VECTOR) return ""

        val xString = if (min.x != 0 || max.x != 0) "X: ${min.x} to ${max.x}" else null
        val yString = if (min.y != 0 || max.y != 0) "Y: ${min.y} to ${max.y}" else null
        val zString = if (min.z != 0 || max.z != 0) "Z: ${min.z} to ${max.z}" else null

        return listOfNotNull(xString, yString, zString).joinToString(" and ")
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