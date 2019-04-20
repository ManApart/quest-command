package combat.block

import combat.battle.position.TargetDirection
import core.events.Event
import core.gameState.Target
import core.gameState.body.BodyPart


class BlockEvent(val source: Target, val part: BodyPart, val direction: TargetDirection) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}