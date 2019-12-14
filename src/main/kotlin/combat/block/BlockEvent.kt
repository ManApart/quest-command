package combat.block

import core.events.Event
import core.target.Target
import core.body.BodyPart


class BlockEvent(val source: Target, val partThatWillShield: BodyPart, val partThatWillBeShielded: BodyPart) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}