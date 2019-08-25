package combat.block

import core.events.Event
import core.gameState.Target
import core.gameState.body.BodyPart


class BlockEvent(val source: Target, val partThatWillShield: BodyPart, val partThatWillBeShielded: BodyPart) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}