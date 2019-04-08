package combat.attack

import combat.Combatant
import combat.battle.position.HitLevel
import combat.battle.position.TargetPosition
import core.gameState.body.BodyPart
import core.utility.random

class AttackedPartHelper(private val defender: Combatant, private val target: TargetPosition) {
    private val adjustedTarget = target.shift(defender.position.invert())

    fun getAttackedPart(): BodyPart? {
        return getBlockedPart()
                ?: getDirectHitPart()
                ?: getGrazedHitPart()
    }

    private fun getBlockedPart(): BodyPart? {
        val blockHitLevel = defender.blockPosition?.getHitLevel(target)
        if (defender.blockBodyPart != null && (blockHitLevel == HitLevel.DIRECT || blockHitLevel == HitLevel.GRAZING)) {
            return defender.blockBodyPart
        }
        return null
    }

    private fun getDirectHitPart(): BodyPart? {
        val parts = defender.creature.body.getDirectParts(adjustedTarget)
        return randomOrNull(parts)
    }

    private fun getGrazedHitPart(): BodyPart? {
        val parts = defender.creature.body.getGrazedParts(adjustedTarget)
        return randomOrNull(parts)
    }

    private fun randomOrNull(parts: List<BodyPart>): BodyPart? {
        return if (parts.isNotEmpty()) {
            parts.random()
        } else {
            null
        }
    }
}