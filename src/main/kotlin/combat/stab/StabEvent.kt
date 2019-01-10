package combat.stab

import combat.battle.position.TargetDirection
import combat.battle.position.TargetPosition
import core.events.Event
import core.gameState.body.BodyPart
import core.gameState.Creature
import core.gameState.Target

class StabEvent(val source: Creature, val sourcePart: BodyPart, val target: Target, val position: TargetPosition) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}