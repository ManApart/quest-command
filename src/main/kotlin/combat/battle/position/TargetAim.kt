package combat.battle.position

import core.gameState.Target
import core.gameState.body.BodyPart

class TargetAim(val target: Target, val bodyPartTargets: List<BodyPart> = listOf()) {
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

