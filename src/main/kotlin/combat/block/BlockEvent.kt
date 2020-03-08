package combat.block

import core.events.Event
import core.target.Target
import traveling.location.location.Location


class BlockEvent(val source: Target, val partThatWillShield: Location, val partThatWillBeShielded: Location) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}