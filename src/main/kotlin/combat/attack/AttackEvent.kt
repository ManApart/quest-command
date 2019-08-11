package combat.attack

import combat.DamageType
import combat.battle.position.TargetPosition
import core.events.Event
import core.gameState.Target
import core.gameState.body.BodyPart


class AttackEvent(val source: Target, val sourcePart: BodyPart, val target: Target, val targetPosition: TargetPosition, val type: DamageType) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}