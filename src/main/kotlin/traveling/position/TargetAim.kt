package traveling.position

import core.target.Target
import traveling.location.location.Location

class TargetAim(val target: Target, val bodyPartTargets: List<Location> = listOf()) {
    override fun toString(): String {
        return if (bodyPartTargets.isEmpty()) {
            target.toString()
        } else {
            bodyPartTargets.joinToString(" ") + " of " + target.toString()
        }
    }

    fun toCommandString(): String {
        return bodyPartTargets.joinToString(" ") { it.name } + " of " + target.name
    }
}

fun List<TargetAim>.toCommandString(): String {
    return joinToString(" and ") { it.toCommandString() }
}

