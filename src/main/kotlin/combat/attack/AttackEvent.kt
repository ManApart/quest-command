package combat.attack

import combat.battle.position.TargetPosition
import core.events.Event
import core.gameState.Creature
import core.gameState.Target
import core.gameState.body.BodyPart
import core.gameState.isPlayer

class AttackEvent(val source: Creature, val sourcePart: BodyPart, val target: Target, val targetPosition: TargetPosition, val type: AttackType) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}