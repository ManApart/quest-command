package combat

import combat.battle.position.TargetPosition
import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Target
import core.gameState.body.BodyPart

class AttackEvent(val source: Creature, val sourcePart: BodyPart, val target: Target, val targetPosition: TargetPosition, val type: AttackType) : Event {
    override fun gameTicks(): Int {
        return if (source == GameState.player.creature) {
            1
        } else {
            0
        }
    }
}