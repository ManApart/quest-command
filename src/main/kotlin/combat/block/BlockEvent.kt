package combat.block

import combat.battle.position.TargetDirection
import core.events.Event
import core.gameState.Creature
import core.gameState.body.BodyPart
import core.gameState.isPlayer

class BlockEvent(val source: Creature, val part: BodyPart, val direction: TargetDirection) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}